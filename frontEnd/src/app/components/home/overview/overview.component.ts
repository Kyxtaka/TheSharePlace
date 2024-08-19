import { Component, OnInit } from '@angular/core';
import { GlobalService } from '../../../services/global/global.service';
import { Privilege } from '../../../services/data/data.service';
import { CommonModule } from '@angular/common';


interface OverviewNeed  {
  username: string,
  status: Privilege[],
  groupsCount: number,
  accountCount: number
}

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent implements OnInit {

  constructor (private globalService: GlobalService) {}
  public show: OverviewNeed | undefined;
  public username: string | null = null; 
  public status: Privilege[] = []; 
  public groupCount: number | null = null; 
  public accountCount: number | null = null; 

  ngOnInit(): void {

    this.globalService.userData$.subscribe({
      next: () => {
        this.username = this.globalService.getCurrentUserData()?.username ?? 'Undefined';
        this.status = this.globalService.getCurrentUserData()?.roles ?? [Privilege.USER];
      }
    })

    this.globalService.group$.subscribe({
      next: () => {
        this.groupCount = this.globalService.getCurrentGroupsArray()?.length ?? 0;
      }
    })

    this.globalService.accounts$.subscribe({
      next: () => {
        this.accountCount = this.globalService.getCurrentAccountArray()?.length ?? 0;
      }
    })

    this.show = {
      username: this.username ?? 'Undefined',
      status: this.status ?? [Privilege.USER],
      groupsCount: this.groupCount ?? 0,
      accountCount: this.accountCount ?? 0
    };

  }
  
}
