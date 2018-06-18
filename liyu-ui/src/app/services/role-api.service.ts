import {Injectable} from '@angular/core';
import {AppHttpClient} from "../libs/http/http-client";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs/internal/Observable";
import {ApiResponse} from "../models/api-response";
import {RoleListResponse} from "../models/role-list-response";
import {RoleResponse} from "../models/role-response";

@Injectable()
export class RoleApiService {
    private readonly baseUrl: string;

    constructor(private http: AppHttpClient) {
        this.baseUrl = environment.roleApi;
    }

    public list(): Observable<RoleListResponse> {
        return this.http.get(this.baseUrl);
    }

    public create(postData: object): Observable<RoleResponse> {
        return this.http.post(this.baseUrl, postData);
    }

    public update(roleId: string, postData: object): Observable<RoleResponse> {
        return this.http.put(`${this.baseUrl}/${roleId}`, postData);
    }

    public delete(roleId: string): Observable<ApiResponse> {
        return this.http.delete(`${this.baseUrl}/${roleId}`);
    }
}
