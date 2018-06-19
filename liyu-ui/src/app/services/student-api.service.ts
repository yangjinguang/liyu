import {Injectable} from '@angular/core';
import {AppHttpClient} from '../libs/http/http-client';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs/internal/Observable';
import {StudentListResponse} from '../models/student-list-response';
import {StudentResponse} from '../models/student-response';
import {StudentStatus} from '../pages/personnel/enums/student-status';

@Injectable()
export class StudentApiService {
    private readonly baseUrl: string;

    constructor(private http: AppHttpClient) {
        this.baseUrl = environment.studentApi;
    }

    public list(searchData?: object): Observable<StudentListResponse> {
        return this.http.get(this.baseUrl, searchData);
    }

    public create(postData: object): Observable<StudentResponse> {
        return this.http.post(`${this.baseUrl}`, postData);
    }

    public update(studentId: string, postData: object): Observable<StudentResponse> {
        return this.http.put(`${this.baseUrl}/${studentId}`, postData);
    }

    public detail(studentId: string): Observable<StudentResponse> {
        return this.http.get(`${this.baseUrl}/${studentId}`);
    }

    public changeStatus(studentId: string, status: StudentStatus): Observable<StudentResponse> {
        return this.http.put(`${this.baseUrl}/${studentId}`, {status: status});
    }
}
