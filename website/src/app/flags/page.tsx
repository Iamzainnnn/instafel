"use client";

import { AnimatePresence, motion } from "framer-motion";
import type React from "react";
import { useEffect, useState } from "react";
import { LoadingBar } from "@/components/LoadingBars";
import Footer from "@/components/Footer";
import { useSearchParams } from "next/navigation";
import Link from "next/link";
import Navbar from "@/components/Navbar";
import { useTranslation } from "react-i18next";

interface FlagListData {
  id: string;
  title: string;
  author: string;
  last_edit: string;
}

export default function FlagListPage() {
  const { t } = useTranslation(["flags", "fcategories"]);
  const [hoveredId] = useState<number | null>(null);

  const searchParams = useSearchParams();
  const categoryName = searchParams.get("category") ?? "0";
  const [data, setData] = useState<FlagListData[] | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(
          `https://raw.githubusercontent.com/instafel/flags/refs/heads/main/list/dir-list/${categoryName}.json`
        );
        const result: [] = await res.json();
        var data: FlagListData[] = [];
        result.forEach((item) =>
          data.push({
            id: item[0],
            title: item[1],
            author: item[2],
            last_edit: item[3],
          })
        );

        setData(data);
      } catch (e) {
        console.error("An error occured while sending / reading response:", e);
      }
    };
    fetchData();
  }, []);

  return (
    <AnimatePresence>
      {data ? (
        <div className="bg-background text-foreground">
          <Navbar />
          <div className="min-h-screen py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-5xl mx-auto">
              <div className="text-center mb-16">
                <motion.h1
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.2,
                    duration: 0.8,
                    ease: "easeInOut",
                  }}
                  className="text-5xl font-bold tracking-tight mb-4 text-foreground"
                >
                  {t(categoryName, { ns: "fcategories" })}
                </motion.h1>

                <motion.div
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.3,
                    duration: 0.8,
                    ease: "easeInOut",
                  }}
                  className="flex items-center justify-center gap-3 text-muted-foreground"
                >
                  <div className="h-[1px] w-10 bg-border" />
                  <p className="text-lg">
                    {t("flag_found", {
                      fcount: data.length,
                    })}
                  </p>
                  <div className="h-[1px] w-10 bg-border" />
                </motion.div>
              </div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.6, duration: 0.8, ease: "easeInOut" }}
                className="grid gap-4 mb-6"
              >
                <div className="grid gap-4 mb-6">
                  {data.map((flag, index) => (
                    <Link
                      key={index}
                      href={`/flag?id=${flag.id}`}
                      className="group relative"
                    >
                      <div
                        className={`
          relative overflow-hidden bg-card rounded-xl border border-border
          transition-all duration-300
          ${hoveredId === index ? "shadow-lg scale-[1.01]" : "hover:shadow-sm"}
        `}
                      >
                        <div className="p-4 sm:p-6">
                          <div className="flex items-start justify-between">
                            <div className="flex-12">
                              <div className="flex items-center gap-3">
                                <h3 className="text-lg font-semibold text-foreground">
                                  {flag.title}
                                </h3>
                              </div>

                              <div className="items-center gap-4 text-sm text-muted-foreground">
                                <div className="flex items-center gap-2">
                                  <svg
                                    className="w-4 h-4"
                                    fill="none"
                                    stroke="currentColor"
                                    viewBox="0 0 24 24"
                                  >
                                    <path
                                      strokeLinecap="round"
                                      strokeLinejoin="round"
                                      strokeWidth={1.5}
                                      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                                    />
                                  </svg>
                                  {t("added_by", { name: flag.author })}
                                </div>
                                <div className="flex items-center gap-2">
                                  <svg
                                    className="w-4 h-4"
                                    fill="none"
                                    stroke="currentColor"
                                    viewBox="0 0 24 24"
                                  >
                                    <path
                                      strokeLinecap="round"
                                      strokeLinejoin="round"
                                      strokeWidth={1.5}
                                      d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
                                    />
                                  </svg>
                                  {t("uploaded_at", {
                                    date_str: flag.last_edit,
                                  })}
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>

                        <div
                          className={`
              absolute inset-0
              bg-gradient-to-tr from-muted/0 via-muted/0 to-muted/50
              transition-opacity duration-300`}
                        />
                      </div>
                    </Link>
                  ))}
                </div>
              </motion.div>
            </div>
          </div>
          <Footer />
        </div>
      ) : (
        <LoadingBar />
      )}
    </AnimatePresence>
  );
}
