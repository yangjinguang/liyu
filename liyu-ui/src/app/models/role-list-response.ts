import {ApiResponse} from "./api-response";
import {Role} from "./role";

export interface RoleListResponse extends ApiResponse {
    data: Role[];
}
