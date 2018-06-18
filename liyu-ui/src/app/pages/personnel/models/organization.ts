import {Contact} from '../../../models/contact';

export interface Organization {
    id: number;
    organizationId: string;
    name: string;
    description: string;
    avatar: string;
    enabled: boolean;
    gradeId: string;
    gradeName: string;
    contactIds: string[];
    contacts: Contact[];
    contactNameTitle: string;
    studentCount: number;
    tenantId: string;
    createdAt: Date;
    updatedAt: Date;
}
