"use client";

import { useState, useEffect } from "react";
import FlagDetailPage from "@/components/FlagDetailPage";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import { LoadingBar } from "@/components/LoadingBars";
import { useTranslation } from "react-i18next";
import { useSearchParams } from "next/navigation";
import matter from "gray-matter";
import MarkdownRenderer from "@/components/MarkdownRenderer";

export default function Page() {
  const { t } = useTranslation("flag");
  const searchParams = useSearchParams();
  const ID = searchParams.get("id") ?? "0";
  const [flagData, setFlagData] = useState(null);
  const [content, setContent] = useState(null);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadFlagData = async () => {
      try {
        const res = await fetch(
          `https://raw.githubusercontent.com/instafel/flags/refs/heads/main/list/mds/${ID}.md`
        );

        const { data: frontmatter, content } = matter(await res.text());
        if (content != "404: Not Found") {
          setFlagData(frontmatter);
          setContent(content);
        }
      } catch (error) {
        console.error("Error loading flag data:", error);
      } finally {
        setLoading(false);
      }
    };
    loadFlagData();
  }, []);

  if (loading) {
    return <LoadingBar />;
  }

  if (!flagData && !content) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen bg-background flex items-center justify-center">
          <p className="text-muted-foreground">{t("not_found")}</p>
        </div>
        <Footer />
      </>
    );
  }

  return (
    <>
      <Navbar />
      <FlagDetailPage flagData={flagData}>
        <div className="space-y-4">
          <MarkdownRenderer
            content={content}
            imgSrc={
              "https://raw.githubusercontent.com/instafel/flags/refs/heads/main/imgs/"
            }
          />
        </div>
      </FlagDetailPage>
      <Footer />;
    </>
  );
}
