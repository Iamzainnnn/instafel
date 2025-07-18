import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64
import java.text.SimpleDateFormat
import java.util.Date

var config = rootProject.extra["instafelConfig"] as Map<*, *>
val projectConfig = config["patcher"] as Map<*, *>
val cliVersion = projectConfig["cli_version"] as String

val commitHash: String by rootProject.extra

tasks.register("publish-cli") {
    doLast {
        val token = System.getenv("GITHUB_TOKEN")
            ?: error("GITHUB_TOKEN environment variable not set")

        val outputDir = file("${rootProject.rootDir}/patcher/output")
        val jarFiles = outputDir.listFiles()?.filter { it.extension == "jar" && it.name.startsWith("ifl-cli") }
            ?: error("Output directory not found or empty")
        if (jarFiles.isEmpty()) error("No jar files found")
        if (jarFiles.size > 1) error("Multiple jar files starting with ifl-cli found")
        val jarFile = jarFiles.first()

        val commit = "git rev-parse --short HEAD".runCommand()?.trim() ?: error("Failed to get git commit")
        val jarBase64 = Base64.getEncoder().encodeToString(jarFile.readBytes())
        val jarPayload = """
            {
              "message": "[PUBLISH] Upload CLI JAR v$cliVersion",
              "content": "$jarBase64",
              "branch": "ft-releases"
            }
        """.trimIndent()
        val jarUploadUrl = "https://api.github.com/repos/mamiiblt/instafel/contents/p-cli/dist/ifl-cli-$cliVersion-$commit-release.jar"
        apiPut(jarUploadUrl, token, jarPayload)

        println("Dist ifl-cli-$commit-release uploaded")

        val ts = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(Date())
        val latestJson = """
            {
              "version": "$cliVersion",
              "commit": "$commitHash",
              "updated_at": "$ts"
            }
        """.trimIndent()

        val latestBase64 = Base64.getEncoder().encodeToString(latestJson.toByteArray())

        val latestGetUrl = "https://api.github.com/repos/mamiiblt/instafel/contents/p-cli/latest.json?ref=ft-releases"
        val existingJsonResponse = try {
            apiGet(latestGetUrl, token)
        } catch (e: Exception) {
            null
        }

        val existingSha = existingJsonResponse
            ?.let { regexFind(it, """"sha"\s*:\s*"([^"]+)"""") }

        val latestPayload = """
            {
              "message": "[PUBLISH] Update latest.json for CLI v$cliVersion",
              "content": "$latestBase64",
              "branch": "ft-releases"
              ${if (existingSha != null) """, "sha": "$existingSha" """ else ""}
            }
        """.trimIndent()

        val latestUploadUrl = "https://api.github.com/repos/mamiiblt/instafel/contents/p-cli/latest.json"

        apiPut(latestUploadUrl, token, latestPayload)

        println("latest.json updated succesfully")
        println("published succesfully")
    }
}

fun apiPut(url: String, token: String, jsonBody: String) {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "PUT"
    connection.setRequestProperty("Authorization", "token $token")
    connection.setRequestProperty("Accept", "application/vnd.github.v3+json")
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")

    connection.outputStream.use { os ->
        os.write(jsonBody.toByteArray(Charsets.UTF_8))
    }

    val responseCode = connection.responseCode
    if (responseCode !in 200..299) {
        val errorStream = connection.errorStream?.bufferedReader()?.readText()
        error("PUT failed with code $responseCode: $errorStream")
    }
}

fun apiGet(url: String, token: String): String {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.setRequestProperty("Authorization", "token $token")
    connection.setRequestProperty("Accept", "application/vnd.github.v3+json")

    val responseCode = connection.responseCode
    if (responseCode !in 200..299) {
        val errorStream = connection.errorStream?.bufferedReader()?.readText()
        error("GET failed with code $responseCode: $errorStream")
    }

    return connection.inputStream.bufferedReader().readText()
}

fun regexFind(input: String, pattern: String): String? {
    val regex = Regex(pattern)
    return regex.find(input)?.groups?.get(1)?.value
}

fun String.runCommand(workingDir: File? = null): String? {
    return try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        proc.inputStream.bufferedReader().readText().trim()
    } catch (e: Exception) {
        null
    }
}