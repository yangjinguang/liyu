import {ApiResponse} from "./api-response";
import {Grade} from "../pages/personnel/models/grade";

export interface GradeListResponse extends ApiResponse {
    data: Grade[];
}
