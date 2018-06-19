import {Pipe, PipeTransform} from '@angular/core';
import {ContactStatusTrans} from '../enums/contact-status';

@Pipe({
    name: 'personnelContactStatusTrans'
})
export class PersonnelContactStatusTransPipe implements PipeTransform {

    transform(status: number): string {
        return ContactStatusTrans[status];
    }

}
