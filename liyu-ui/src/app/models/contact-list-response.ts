import {ApiResponse} from './api-response';
import {Pagination} from './pagination';
import {Contact} from "./contact";

export interface ContactListResponse extends ApiResponse {
    data: {
        list: Contact[];
        pagination: Pagination;
    };
}
