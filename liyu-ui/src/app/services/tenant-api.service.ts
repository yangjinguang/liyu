import {Injectable} from '@angular/core';
import {AppHttpClient} from '../libs/http/http-client';
import {environment} from '../../environments/environment';
import {TenantListResponse} from '../models/tenant-list-response';
import {Observable} from "rxjs/internal/Observable";

@Injectable()
export class TenantApiService {
    private readonly baseUrl: string;

    constructor(private http: AppHttpClient) {
        this.baseUrl = environment.tenantApi;
    }

    public listCurrent(): Observable<TenantListResponse> {
        return this.http.get(`${this.baseUrl}/current`);
    }

}
