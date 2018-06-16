import {ApiResponse} from './api-response';
import {Account} from "./account";

export interface AccountProfileResponse extends ApiResponse {
    data: Account;
}
