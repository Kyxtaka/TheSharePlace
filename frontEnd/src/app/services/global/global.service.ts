import { Injectable,  } from '@angular/core';
import { Credential, UserData, Group, Privilege, Platform} from '../data/data.service';
import { APIServiceAccount, APIServiceGroup, APIServiceUser, APIServicePlatform } from "../data/data.service"
import { BehaviorSubject,Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class GlobalService {

  //Login data + status
  private isLogged: BehaviorSubject<boolean>;
  public isLogged$: Observable<Boolean>;

  private userDataSubject: BehaviorSubject<UserData | null>;
  public userData$: Observable<UserData | null>;

  private accountSubject: BehaviorSubject<Credential[]>;
  public accounts$: Observable<Credential[]>;

  private groupSubject: BehaviorSubject<Group[]>;
  public group$: Observable<Group[]>;

  private paltformSubject: BehaviorSubject<Platform[]>;
  public platform$: Observable<Platform[]>;

 
 
  constructor( 
    private APIServiceAccount: APIServiceAccount,  
    private APIServiceGroup:  APIServiceGroup,
    private APIServiceUser: APIServiceUser,
    private APIServicePlatform: APIServicePlatform
  ) {

    this.isLogged = new BehaviorSubject<boolean>(false);
    this.isLogged$ = this.isLogged.asObservable();

    this.userDataSubject = new BehaviorSubject<UserData | null>(null);
    this.userData$ = this.userDataSubject.asObservable();
    
    this.accountSubject = new BehaviorSubject<Credential[]>([]);
    this.accounts$ = this.accountSubject.asObservable();

    this.groupSubject = new BehaviorSubject<Group[]>([]);
    this.group$ = this.groupSubject.asObservable();

    this.paltformSubject = new BehaviorSubject<Platform[]>([]);
    this.platform$ = this.paltformSubject.asObservable();

  }

  //isLogged
  public updateIsLogged(data: boolean) {  this.isLogged.next(data); }
  public getCurrentIsLogged(): boolean {return this.isLogged.getValue();}

  // userData
  public updateUserData(data: UserData | null): void {this.userDataSubject.next(data);}
  public getCurrentUserData(): UserData | null {return this.userDataSubject.getValue();}

  //Groups
  public getCurrentGroupsArray(): Group[] {return this.groupSubject.getValue();}
  public updateGroupsArray(data: Group): void {
    const tmp: Group[] = this.groupSubject.getValue();
    const execute: boolean = !tmp.some( elem => 
      elem.id === data.id &&
      elem.UID === data.UID &&
      elem.name === data.name &&
      elem.description === data.description &&
      elem.password === data.password
    )
    if (execute) {tmp.push(data)}
    this.groupSubject.next(tmp);
  }

  //Account
  public getCurrentAccountArray(): Credential[] {return this.accountSubject.getValue();}
  public updateAccountArray(data:Credential): void {
    const tmp: Credential[] = this.accountSubject.getValue();
    const exists = tmp.some(elem => 
      elem.id === data.id && 
      elem.identifier === data.identifier && 
      elem.email === data.email && 
      elem.password === data.password && 
      elem.a2f === data.a2f && 
      elem.platformId === data.platformId && 
      elem.groupId === data.groupId
    ); // Check if the data already exists in the array
    if (!exists) {tmp.push(data);} // If it does not exist, push the new data
    this.accountSubject.next(tmp); // Notify the subject with the updated array
  }

  public getCurrentPlatformArray(): Platform[] {return this.paltformSubject.getValue();}
  public updatePlatformArray(data:Platform): void {
    const tmp: Platform[] = this.paltformSubject.getValue();
    const exists = tmp.some(elem => 
      elem.id === data.id && 
      elem.name === data.name && 
      elem.url === data.url && 
      elem.imgRef === data.imgRef 
    ); // Check if the data already exists in the array
    if (!exists) {tmp.push(data);} // If it does not exist, push the new data
    this.paltformSubject.next(tmp); // Notify the subject with the updated array
  }

  //Truncate Array
  public emptyArraySubject(arraySubject: any): void {arraySubject.next([]);} 

  // Global Data Initializer
  //Initialisation des data pour l user
  public init(username: string): void {
    this.APIServiceUser.findByUsername(username).subscribe({
      next: (response) => {
        const roles: Privilege[] = []
        response['roles'].forEach((role: {[x:string]: string} ) => {
          if (role['name'] === "ADMIN") {roles.push(Privilege.ADMIN)}
          else {roles.push(Privilege.USER)}
        });
        const userData: UserData = {
          userId: response['id'],
          username: response['username'],
          email: response['email']['mailAddress'],
          firstname: response['firstname'],
          lastname: response['lastname'],
          roles: roles,
          groups: response['groups']
        }
        this.updateUserData(userData);
        this.updateIsLogged(true)
        this.initGroupsAndAcccounts(this.getCurrentUserData()!)
        // this.initGroupsAndAcccounts(this.userDataSubject.getValue()!)
      },
      error: (error) => {
        console.log("Error occrured while initializing userData:", error)
      }
    })
  }

  //Initalisation des Group et Comptes
  public initGroupsAndAcccounts(userData: UserData): void {
    if (this.getCurrentUserData() == null || this.getCurrentIsLogged() == false) {this.disconnect()}
    userData.groups.forEach( group => {
      this.updateGroupsArray(group);
      this.APIServiceAccount.findBy("group",group.id).subscribe({
        next: (response) => {
            response.forEach((element: {[x:string]: any}) => {
                const account: Credential = {
                    id: element['id'],
                    email: element['email']['mailAddress'],
                    identifier: element['username'],
                    password: element['password'],
                    a2f: element['a2f'],
                    groupId: element['group']['id'],
                    platformId: element['platform']['id']
                }
                const platform: Platform = element['platform']
                this.updatePlatformArray(platform)
                this.updateAccountArray(account);
            });
        },
        error: (error) => {
          console.log("Erreur recup compte :", error)
        }
      })
    })
    
  } 

  public disconnect() {
    this.updateUserData(null)
    this.emptyArraySubject(this.accountSubject)
    this.emptyArraySubject(this.groupSubject)
    this.emptyArraySubject(this.paltformSubject)
    this.updateIsLogged(false)
  }

}
