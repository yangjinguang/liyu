import {ApiResponse} from "./api-response";
import {Grade} from "../pages/personnel/models/grade";

export interface GradeResponse extends ApiResponse {
    data: Grade;
}
