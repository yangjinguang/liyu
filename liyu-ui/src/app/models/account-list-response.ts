import {ApiResponse} from './api-response';
import {Pagination} from './pagination';
import {Account} from "./account";

export interface AccountListResponse extends ApiResponse {
    data: {
        list: Account[];
        pagination: Pagination;
    };
}
