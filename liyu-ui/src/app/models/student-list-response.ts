import {ApiResponse} from './api-response';
import {Student} from '../pages/personnel/models/student';
import {Pagination} from './pagination';

export interface StudentListResponse extends ApiResponse {
    data: {
        list: Student[];
        pagination: Pagination
    }
}
