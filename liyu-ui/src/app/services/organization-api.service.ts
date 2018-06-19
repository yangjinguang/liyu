import {Injectable} from '@angular/core';
import {AppHttpClient} from '../libs/http/http-client';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs/internal/Observable';
import {OrganizationListResponse} from '../models/organization-list-response';
import {OrganizationResponse} from '../models/organization-response';
import {ApiResponse} from '../models/api-response';

@Injectable()
export class OrganizationApiService {
    private readonly baseUrl: string;

    constructor(private http: AppHttpClient) {
        this.baseUrl = environment.organizationApi;
    }

    public list(page: number, size: number, searchText?: string): Observable<OrganizationListResponse> {
        return this.http.get(this.baseUrl, {page: page, size: size, searchText: searchText});
    }

    public miniList(page: number, size: number, searchText?: string): Observable<OrganizationListResponse> {
        return this.http.get(`${this.baseUrl}/mini`, {page: page, size: size, searchText: searchText});
    }

    public create(postData: object): Observable<OrganizationResponse> {
        return this.http.post(this.baseUrl, postData);
    }

    public update(organizationId: string, postData: object): Observable<OrganizationResponse> {
        return this.http.put(`${this.baseUrl}/${organizationId}`, postData);
    }

    public delete(organizationId: string): Observable<ApiResponse> {
        return this.http.delete(`${this.baseUrl}/${organizationId}`);
    }
}
