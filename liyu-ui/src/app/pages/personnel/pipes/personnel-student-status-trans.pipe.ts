import {Pipe, PipeTransform} from '@angular/core';
import {StudentStatusTrans} from '../enums/student-status';

@Pipe({
    name: 'personnelStudentStatusTrans'
})
export class PersonnelStudentStatusTransPipe implements PipeTransform {

    transform(status: number): string {
        return StudentStatusTrans[status];
    }

}
