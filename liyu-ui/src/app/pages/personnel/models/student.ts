import {Organization} from './organization';

export interface Student {
    id: number;
    studentId: string;
    name: string;
    gender:number;
    phone: string;
    avatar: string;
    profileId: string;
    birthday: Date;
    status: number;
    tenantId: string;
    organizationId: string;
    organization: Organization;
    createdAt: Date;
    updatedAt: Date;
}
