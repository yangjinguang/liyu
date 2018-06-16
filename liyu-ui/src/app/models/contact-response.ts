import {ApiResponse} from './api-response';
import {Contact} from "./contact";

export interface ContactResponse extends ApiResponse {
    data: Contact;
}
