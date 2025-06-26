"use client";

import { AnimatePresence, motion } from "framer-motion";
import { useState } from "react";
import Footer from "@/components/Footer";
import { useTranslation } from "react-i18next";
import Navbar from "@/components/Navbar";
import Link from "next/link";
import { flagCategories } from "@/wdata/flag_sdata";
import { FlagIcon } from "lucide-react";

export default function LibraryBackupPage() {
  const { t } = useTranslation(["library_flag", "fcategories"]);
  const [hoveredId, setHoveredId] = useState<number | null>(null);

  return (
    <AnimatePresence>
      <div>
        <Navbar />
        <div className="min-h-screen bg-background py-12 px-4 sm:px-6 lg:px-8">
          <div className="max-w-6xl mx-auto">
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
              className="text-center mb-12"
            >
              <div className="flex justify-center mb-6">
                <div className="relative">
                  <div
                    className="absolute inset-0 rounded-full bg-primary/20 animate-ping"
                    style={{ animationDuration: "3s" }}
                  ></div>
                  <div className="relative bg-primary/30 p-5 rounded-full">
                    <FlagIcon className="h-12 w-12 text-primary" />
                  </div>
                </div>
              </div>
              <h1 className="text-4xl font-bold mb-4 text-foreground">
                {t("title")}
              </h1>
              <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
                {t("desc")}
              </p>
            </motion.div>

            <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
              {flagCategories.map((category, idx) => {
                return (
                  <motion.div
                    key={idx}
                    initial={{ opacity: 0, y: 30 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{
                      delay: 0.4 + idx * 0.1,
                      duration: 0.8,
                      ease: "easeOut",
                    }}
                  >
                    <Link
                      href={`/flags?category=${category.cif}`}
                      onMouseEnter={() => setHoveredId(idx)}
                      onMouseLeave={() => setHoveredId(null)}
                    >
                      <button
                        className={`
                            w-full p-4 h-[160px]
                            flex flex-col items-center justify-center text-center
                            rounded-xl border border-border transition-all duration-300
                            bg-card text-foreground
                            hover:shadow-lg hover:scale-105
                          `}
                      >
                        <div
                          className={`
                              mb-3 transform transition-transform duration-300
                              ${hoveredId === idx ? "scale-110" : ""}
                            `}
                        >
                          {category.icon}
                        </div>

                        <h3 className="font-medium mb-1 transition-colors duration-300">
                          {t(`${category.cif}`, { ns: "fcategories" })}
                        </h3>
                      </button>
                    </Link>
                  </motion.div>
                );
              })}
            </div>
          </div>
        </div>
        <Footer />
      </div>
    </AnimatePresence>
  );
}
