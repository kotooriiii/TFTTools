export interface Tool {
  id: string;
  name: string;
  icon: string;
  component: React.ComponentType<any>;
  description?: string;
}