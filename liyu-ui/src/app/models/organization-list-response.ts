import {ApiResponse} from './api-response';
import {Pagination} from './pagination';
import {Organization} from "../pages/personnel/models/organization";

export interface OrganizationListResponse extends ApiResponse {
    data: {
        list: Organization[];
        pagination: Pagination;
    };
}
