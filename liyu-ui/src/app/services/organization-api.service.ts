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

    public list(searchData?: object): Observable<OrganizationListResponse> {
        return this.http.get(this.baseUrl, searchData);
    }

    public miniList(searchData?: object): Observable<OrganizationListResponse> {
        return this.http.get(`${this.baseUrl}/mini`, searchData);
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
