import {ApiResponse} from './api-response';
import {Account} from "./account";

export interface AccountResponse extends ApiResponse {
    data: Account;
}
