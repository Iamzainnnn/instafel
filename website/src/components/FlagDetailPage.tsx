"use client";

import React from "react";
import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import {
  CalendarIcon,
  UserIcon,
  TagIcon,
  AlertTriangleIcon,
  PlusIcon,
  MinusIcon,
  FlagIcon,
} from "lucide-react";
import { useTranslation } from "react-i18next";

interface FlagData {
  id: string;
  category: string;
  title: string;
  desc: string;
  author: string;
  added_in: string;
  removed_in?: string;
  uncompitable_flags: string[];
  last_edit: string;
}

interface FlagDetailPageProps {
  flagData: FlagData;
  children: React.ReactNode;
}

const FlagDetailPage: React.FC<FlagDetailPageProps> = ({
  flagData,
  children,
}) => {
  const { t } = useTranslation(["flag", "fcategories"]);

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        duration: 0.6,
        ease: "easeOut",
        staggerChildren: 0.1,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: {
      opacity: 1,
      y: 0,
      transition: {
        duration: 0.5,
        ease: "easeOut",
      },
    },
  };

  const badgeVariants = {
    hidden: { opacity: 0, scale: 0.8 },
    visible: {
      opacity: 1,
      scale: 1,
      transition: {
        duration: 0.4,
        ease: "easeOut",
      },
    },
  };

  return (
    <div className="min-h-screen bg-background text-foreground">
      <motion.div
        className="container mx-auto p-6 max-w-4xl"
        variants={containerVariants}
        initial="hidden"
        animate="visible"
      >
        <motion.div variants={itemVariants}>
          <Card className="mb-6 border-border bg-card">
            <CardHeader className="pb-4">
              <div className="flex items-start justify-between">
                <div className="space-y-2">
                  <motion.div
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ duration: 0.6, ease: "easeOut", delay: 0.2 }}
                  >
                    <CardTitle className="text-2xl font-bold text-card-foreground">
                      {flagData.title}
                    </CardTitle>
                  </motion.div>
                  <motion.div
                    className="flex items-center gap-2"
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ duration: 0.6, ease: "easeOut", delay: 0.3 }}
                  >
                    <motion.div variants={badgeVariants}>
                      <Badge
                        variant={"default"}
                        className="bg-secondary text-secondary-foreground transition-colors hover:bg-secondary/80"
                      >
                        <TagIcon className="w-3 h-3 mr-1" />
                        {t(flagData.category, { ns: "fcategories" })}
                      </Badge>
                    </motion.div>
                  </motion.div>
                </div>
              </div>
            </CardHeader>

            <CardContent className="space-y-4">
              <motion.div
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5, ease: "easeOut", delay: 0.4 }}
              >
                <p className="text-muted-foreground leading-relaxed">
                  {flagData.desc}
                </p>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, scaleX: 0 }}
                animate={{ opacity: 1, scaleX: 1 }}
                transition={{ duration: 0.4, ease: "easeOut", delay: 0.5 }}
              >
                <Separator className="bg-border" />
              </motion.div>

              <motion.div
                className="grid grid-cols-1 md:grid-cols-2 gap-4"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 0.6, ease: "easeOut", delay: 0.6 }}
              >
                <motion.div
                  className="flex items-center gap-3"
                  whileHover={{ scale: 1.02 }}
                  transition={{ duration: 0.2, ease: "easeOut" }}
                >
                  <UserIcon className="w-4 h-4 text-muted-foreground" />
                  <div>
                    <p className="text-sm font-medium text-card-foreground">
                      {t("added_by")}
                    </p>
                    <p className="text-sm text-muted-foreground">
                      {flagData.author}
                    </p>
                  </div>
                </motion.div>

                <motion.div
                  className="flex items-center gap-3"
                  whileHover={{ scale: 1.02 }}
                  transition={{ duration: 0.2, ease: "easeOut" }}
                >
                  <CalendarIcon className="w-4 h-4 text-muted-foreground" />
                  <div>
                    <p className="text-sm font-medium text-card-foreground">
                      {t("last_edit")}
                    </p>
                    <p className="text-sm text-muted-foreground">
                      {flagData.last_edit}
                    </p>
                  </div>
                </motion.div>

                {flagData.added_in && (
                  <motion.div
                    className="flex items-center gap-3"
                    whileHover={{ scale: 1.02 }}
                    transition={{ duration: 0.2, ease: "easeOut" }}
                  >
                    <PlusIcon className="w-4 h-4 text-green-500" />
                    <div>
                      <p className="text-sm font-medium text-card-foreground">
                        {t("added_in")}
                      </p>
                      <p className="text-sm text-muted-foreground font-mono">
                        {flagData.added_in}
                      </p>
                    </div>
                  </motion.div>
                )}

                {flagData.removed_in && (
                  <motion.div
                    className="flex items-center gap-3"
                    whileHover={{ scale: 1.02 }}
                    transition={{ duration: 0.2, ease: "easeOut" }}
                  >
                    <MinusIcon className="w-4 h-4 text-red-500" />
                    <div>
                      <p className="text-sm font-medium text-card-foreground">
                        {t("removed_in")}
                      </p>
                      <p className="text-sm text-muted-foreground font-mono">
                        {flagData.removed_in}
                      </p>
                    </div>
                  </motion.div>
                )}
              </motion.div>

              {flagData.uncompitable_flags && (
                <>
                  <motion.div
                    initial={{ opacity: 0, scaleX: 0 }}
                    animate={{ opacity: 1, scaleX: 1 }}
                    transition={{ duration: 0.4, ease: "easeOut", delay: 0.7 }}
                  >
                    <Separator className="bg-border" />
                  </motion.div>
                  <motion.div
                    className="space-y-3"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, ease: "easeOut", delay: 0.8 }}
                  >
                    <div className="flex items-center gap-2">
                      <AlertTriangleIcon className="w-4 h-4 text-amber-500" />
                      <p className="text-sm font-medium text-card-foreground">
                        {t("incomp_flags")}
                      </p>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      {flagData.uncompitable_flags.map((flag, index) => (
                        <motion.div
                          key={index}
                          initial={{ opacity: 0, scale: 0.8 }}
                          animate={{ opacity: 1, scale: 1 }}
                          transition={{
                            duration: 0.4,
                            ease: "easeOut",
                            delay: 0.9 + index * 0.1,
                          }}
                          whileHover={{ scale: 1.05 }}
                        >
                          <Badge
                            variant="destructive"
                            className="bg-destructive/10 text-destructive border-destructive/20 transition-colors hover:bg-destructive/15"
                          >
                            {flag}
                          </Badge>
                        </motion.div>
                      ))}
                    </div>
                  </motion.div>
                </>
              )}
            </CardContent>
          </Card>
        </motion.div>

        <motion.div
          variants={itemVariants}
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, ease: "easeOut", delay: 0.3 }}
        >
          <Card className="border-border bg-card">
            <CardContent className="p-6">
              <motion.div
                className="prose prose-neutral dark:prose-invert max-w-none"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 0.8, ease: "easeOut", delay: 0.5 }}
              >
                {children}
              </motion.div>
            </CardContent>
          </Card>
        </motion.div>
      </motion.div>
    </div>
  );
};

export default FlagDetailPage;
