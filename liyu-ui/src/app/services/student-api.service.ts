import {Injectable} from '@angular/core';
import {AppHttpClient} from '../libs/http/http-client';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs/internal/Observable';
import {StudentListResponse} from '../models/student-list-response';
import {StudentResponse} from '../models/student-response';

@Injectable()
export class StudentApiService {
    private readonly baseUrl: string;

    constructor(private http: AppHttpClient) {
        this.baseUrl = environment.studentApi;
    }

    public list(page: number, size: number, searchText?: string): Observable<StudentListResponse> {
        return this.http.get(this.baseUrl, {page: page, size: size, searchText: searchText});
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
}
