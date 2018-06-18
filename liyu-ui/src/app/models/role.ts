export interface Role {
    id: number;
    roleId: string;
    name: string;
    description: string;
    tenantId: string;
    enabled: boolean;
    locked: boolean;
    createdAt: Date;
    updatedAt: Date;
}
