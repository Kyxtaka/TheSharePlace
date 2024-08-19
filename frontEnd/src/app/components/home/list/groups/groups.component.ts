import { Component, OnInit} from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { GlobalService } from '../../../../services/global/global.service';
import { Group } from '../../../../services/data/data.service';
import { CommonModule } from '@angular/common';
import { error } from 'console';
import { ReplaySubject } from 'rxjs';


@Component({
  selector: 'app-groups',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './groups.component.html',
  styleUrl: './groups.component.css'
})
export class GroupsComponent  implements OnInit{
  groupsArray: Group[] = []
  
  constructor(
    private router:Router, 
    private globalService: GlobalService, 
    // private dataService: DataService
  ) {}

  redirect(groupName:string): void {
    // this.globalService.updateSelectedGroupId(groupId)
    var result: string = "Null"
    this.groupsArray.forEach( group => {
      if (group.name === groupName) {
        result = group.name;
      }
    })
    console.log("should redirect into group :",result, "component");
    this.router.navigate(['/group']);
  }

  ngOnInit(): void {
    if (this.globalService.getCurrentUserData() == null || !this.globalService.getCurrentIsLogged()) {this.router.navigate(['/'])}

    this.globalService.group$.subscribe({
      next: (data) => {this.groupsArray = data},
      error: (error) => {console.log("Groups page error", error)}
    })
  
  }
}
