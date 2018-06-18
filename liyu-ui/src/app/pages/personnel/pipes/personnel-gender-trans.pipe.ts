import {Pipe, PipeTransform} from '@angular/core';
import {Genders} from '../consts/genders';

@Pipe({
    name: 'personnelGenderTrans'
})
export class PersonnelGenderTransPipe implements PipeTransform {

    transform(gender: number): string {
        return Genders[gender];
    }

}
