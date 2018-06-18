import {ApiResponse} from "./api-response";
import {Role} from "./role";

export interface RoleResponse extends ApiResponse {
    data: Role;
}
