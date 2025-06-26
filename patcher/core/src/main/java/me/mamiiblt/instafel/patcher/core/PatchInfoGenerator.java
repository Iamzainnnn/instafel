package me.mamiiblt.instafel.patcher.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import me.mamiiblt.instafel.patcher.core.utils.models.PatchGroupInfoVar;
import me.mamiiblt.instafel.patcher.core.utils.models.PatchInfoVar;
import me.mamiiblt.instafel.patcher.core.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.core.utils.patch.InstafelPatchGroup;
import me.mamiiblt.instafel.patcher.core.utils.patch.PInfos;
import me.mamiiblt.instafel.patcher.core.utils.patch.PInfos.PatchGroupInfo;
import me.mamiiblt.instafel.patcher.core.utils.patch.PInfos.PatchInfo;

// This generator is only needs to use in build-time.

public class PatchInfoGenerator {
    public static final String DEFAULT_PACKAGE_NAME = "me.mamiiblt.instafel.patcher.core.patches";

    public static List<PatchInfoVar> getPatchInfos() {
        List<PatchInfoVar> patchInfos = new ArrayList<>();
        Set<Class<? extends InstafelPatch>> patches = findPatchClassesInPackage();
        for (Class<? extends InstafelPatch> patch : patches) {
            PatchInfo pInfo = patch.getAnnotation(PatchInfo.class);
            PatchInfoVar i = new PatchInfoVar();
            i.author = pInfo.author();
            i.name = pInfo.name();
            i.desc = pInfo.desc();
            i.shortname = pInfo.shortname();
            i.isSingle = pInfo.isSingle();
            i.className = patch.getName();
            patchInfos.add(i);
        }
        return patchInfos;
    }

    public static List<PatchGroupInfoVar> getPatchGroupInfos() {
        List<PatchGroupInfoVar> patchGroupInfos = new ArrayList<>();
        Set<Class<? extends InstafelPatchGroup>> groups = findPatchGroupClassesInPackage();
        for (Class<? extends InstafelPatchGroup> group : groups) {
            PatchGroupInfo gInfo = group.getAnnotation(PatchGroupInfo.class);
            PatchGroupInfoVar i = new PatchGroupInfoVar();
            i.name = gInfo.name();
            i.shortName = gInfo.shortname();
            i.author = gInfo.author();
            patchGroupInfos.add(i);
        }
        return patchGroupInfos;
    }

    @SuppressWarnings("unchecked")
    public static Set<Class<? extends InstafelPatch>> findPatchClassesInPackage() {
        Set<Class<? extends InstafelPatch>> patches = new HashSet<>();

        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages(DEFAULT_PACKAGE_NAME) 
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()) {

            for (ClassInfo classInfo : scanResult.getSubclasses(InstafelPatch.class.getName())) {
                Class<?> clazz = classInfo.loadClass();
                if (InstafelPatch.class.isAssignableFrom(clazz)) {
                    patches.add((Class<? extends InstafelPatch>) clazz);
                }
            }
        }
        return patches;
    }

    @SuppressWarnings("unchecked")
    public static Set<Class<? extends InstafelPatchGroup>> findPatchGroupClassesInPackage() {
        Set<Class<? extends InstafelPatchGroup>> groups = new HashSet<>();

        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages(DEFAULT_PACKAGE_NAME) 
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()) {

            for (ClassInfo classInfo : scanResult.getSubclasses(InstafelPatchGroup.class.getName())) {
                Class<?> clazz = classInfo.loadClass();
                if (InstafelPatchGroup.class.isAssignableFrom(clazz)) {
                    groups.add((Class<? extends InstafelPatchGroup>) clazz);
                }
            }
        }

        return groups; 
    }

    public static InstafelPatchGroup findPatchGroupByShortname(String shortName) {
        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages(DEFAULT_PACKAGE_NAME)
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()) {

            for (ClassInfo classInfo : scanResult.getSubclasses(InstafelPatchGroup.class.getName())) {
                Class<?> clazz = Class.forName(classInfo.getName());

                if (!Modifier.isAbstract(clazz.getModifiers()) && clazz.isAnnotationPresent(PInfos.PatchGroupInfo.class)) {
                    PInfos.PatchGroupInfo info = clazz.getAnnotation(PInfos.PatchGroupInfo.class);
                    
                    if (shortName.equals("all")) {
                        Constructor<?> constructor = clazz.getDeclaredConstructor();
                        return (InstafelPatchGroup) constructor.newInstance();
                    } else {
                        if (info.shortname().equals(shortName)) {
                            Constructor<?> constructor = clazz.getDeclaredConstructor();
                            return (InstafelPatchGroup) constructor.newInstance();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
}
