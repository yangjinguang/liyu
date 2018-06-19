import {Injectable} from '@angular/core';
import {AppHttpClient} from '../libs/http/http-client';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs/internal/Observable';
import {ContactListResponse} from '../models/contact-list-response';
import {ContactResponse} from '../models/contact-response';
import {ContactStatus} from '../enums/contactStatus';
import {ApiResponse} from '../models/api-response';

@Injectable()
export class ContactApiService {
    private readonly baseUrl: string;

    constructor(private http: AppHttpClient) {
        this.baseUrl = environment.contactApi;
    }

    public profile(): Observable<ContactResponse> {
        return this.http.get(`${this.baseUrl}/profile`);
    }

    public list(page: number, size: number, searchText?: string): Observable<ContactListResponse> {
        return this.http.get(this.baseUrl, {page: page, size: size, searchText: searchText});
    }

    public create(postData: object): Observable<ContactResponse> {
        return this.http.post(`${this.baseUrl}`, postData);
    }

    public update(contactId: string, postData: object): Observable<ContactResponse> {
        return this.http.put(`${this.baseUrl}/${contactId}`, postData);
    }

    public detail(id: number): Observable<ContactResponse> {
        return this.http.get(`${this.baseUrl}/${id}`);
    }

    public changeStatus(contactId: string, status: ContactStatus): Observable<ContactResponse> {
        return this.http.put(`${this.baseUrl}/${contactId}`, {status: status});
    }
}
