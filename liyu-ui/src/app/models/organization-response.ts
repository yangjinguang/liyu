import {ApiResponse} from "./api-response";
import {Organization} from "../pages/personnel/models/organization";

export interface OrganizationResponse extends ApiResponse {
    data: Organization;
}
