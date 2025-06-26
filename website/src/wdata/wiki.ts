import { LucideIcon } from "lucide-react";

export interface WikiPage {
  slug: string;
  title: string;
  description: string;
  writer: string;
}

export interface WikiSubCategory {
  name: string;
  icon: LucideIcon;
  pages: WikiPage[];
}

export interface WikiCategory {
  name: string;
  subs: WikiSubCategory[];
}
