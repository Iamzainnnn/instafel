"use client";

import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import { Globe } from "lucide-react";

export default function WikiLanguageWarning() {
  return (
    <Button
      variant={"ghost"}
      size="icon"
      className="relative dark"
      onClick={() =>
        toast("Translations currently aren't available in Wiki contents!", {
          description:
            "But, you can use Google Translator or other extensions for translate pages!",
          action: {
            label: "Okay!",
            onClick: () => {},
          },
        })
      }
    >
      <Globe className="h-4 w-4" />
    </Button>
  );
}
