import {Injectable} from '@angular/core';
import {AppHttpClient} from '../libs/http/http-client';
import {environment} from '../../environments/environment';
import {LoginResponse} from '../models/login-response';
import {Observable} from "rxjs/internal/Observable";

@Injectable()
export class LoginApiService {
    private readonly baseUrl: string;

    constructor(private http: AppHttpClient) {
        this.baseUrl = environment.loginApi;
    }

    public login(data: object): Observable<LoginResponse> {
        return this.http.post(this.baseUrl, data);
    }
}
