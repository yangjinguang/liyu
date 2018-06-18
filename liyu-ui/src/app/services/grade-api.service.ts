import {Injectable} from '@angular/core';
import {AppHttpClient} from "../libs/http/http-client";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs/internal/Observable";
import {ApiResponse} from "../models/api-response";
import {GradeListResponse} from "../models/grade-list-response";
import {GradeResponse} from "../models/grade-response";

@Injectable()
export class GradeApiService {
    private readonly baseUrl: string;

    constructor(private http: AppHttpClient) {
        this.baseUrl = environment.gradeApi;
    }

    public list(): Observable<GradeListResponse> {
        return this.http.get(this.baseUrl);
    }

    public create(postData: object): Observable<GradeResponse> {
        return this.http.post(this.baseUrl, postData);
    }

    public update(organizationId: string, postData: object): Observable<GradeResponse> {
        return this.http.put(`${this.baseUrl}/${organizationId}`, postData);
    }

    public delete(organizationId: string): Observable<ApiResponse> {
        return this.http.delete(`${this.baseUrl}/${organizationId}`);
    }
}
