import {Pipe, PipeTransform} from '@angular/core';
import {Role} from '../../../models/role';

@Pipe({
    name: 'personnelRoleTrans'
})
export class PersonnelRoleTransPipe implements PipeTransform {

    transform(roleId: string, roles: Role[]): string {
        const role = roles.find(i => i.roleId === roleId);
        return role ? role.name : '';
    }

}
