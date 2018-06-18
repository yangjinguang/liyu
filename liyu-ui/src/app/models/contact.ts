import {ContactStatus} from '../enums/contactStatus';
import {Organization} from '../pages/personnel/models/organization';

export interface Contact {
    id: number;
    contactId: string;
    accountId: string;
    name: string;
    gender: number;
    email: string;
    phone: string;
    avatar: string;
    tenantId: string;
    roleId: string;
    status: ContactStatus;
    organizationIds: string[];
    organizations: Organization[];
    organizationTitle: string;
    createdAt: Date;
    updatedAt: Date;
}
