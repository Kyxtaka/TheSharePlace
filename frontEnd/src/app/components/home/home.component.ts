import { Component, OnInit, ViewEncapsulation} from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { GlobalService } from '../../services/global/global.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  encapsulation: ViewEncapsulation.None

})

export class HomeComponent implements OnInit{

  constructor(
    private router:Router, 
    private globalService: GlobalService,
  ) {}

  username: string = "";
  
  ngOnInit(): void {
    //redirection au login l'utilisateur ne s'est pas connecter
    console.log("Home isLogged : ",this.globalService.getCurrentIsLogged())
    if (this.globalService.getCurrentUserData() == null || !(this.globalService.getCurrentIsLogged())) {
      this.router.navigate(['/']);
    }
    this.username = this.globalService.getCurrentUserData()!.username;
    this.router.navigate(['/home/overview']); 
  }
}

