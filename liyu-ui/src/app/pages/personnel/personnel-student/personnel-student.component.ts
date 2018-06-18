import {Component, OnInit} from '@angular/core';
import {XBreadCrumbService} from "../../../components/x-bread-crumb/x-bread-crumb.service";

@Component({
    selector: 'app-personnel-student',
    templateUrl: './personnel-student.component.html',
    styleUrls: ['./personnel-student.component.less']
})
export class PersonnelStudentComponent implements OnInit {

    constructor(private bcService: XBreadCrumbService) {
    }

    ngOnInit() {
        this.bcService.setItems([
            {
                text: '人员管理',
                link: '/pages/personnel'
            },
            {
                text: '幼儿管理'
            }
        ])
    }

}
