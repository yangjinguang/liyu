import {ApiResponse} from './api-response';
import {Student} from '../pages/personnel/models/student';

export interface StudentResponse extends ApiResponse {
    data: Student;
}
