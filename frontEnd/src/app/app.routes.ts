import { Routes } from '@angular/router';
import { LoginBoxComponent } from './components/login/login-box.component';
import { HomeComponent } from './components/home/home.component';
import { GroupsComponent } from './components/home/list/groups/groups.component';
import { CredentialsComponent } from './components/home/list/credentials/credentials.component';
import { OverviewComponent } from './components/home/overview/overview.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { DisconectComponent } from './components/disconect/disconect.component';
import { UserRegisterComponent } from './components/user-register/user-register.component';
import { CreateAccountFormComponent } from './components/home/create-account-form/create-account-form.component';
import { CreateGroupFormComponent } from './components/home/create-group-form/create-group-form.component';

export const routes: Routes = [ 
    {
        path: '',
        component: WelcomeComponent,
    },
    {
        path: 'disconect',
        component: DisconectComponent
    },
    {
        path: 'login',
        component: LoginBoxComponent
    },
    {
        path: 'register',
        component: UserRegisterComponent
    },
    {
        path: 'home',
        component: HomeComponent,
        children: [
            {
                path: '',
                redirectTo: 'overview',
                pathMatch: 'full',
            },
            {
                path: 'overview',
                component: OverviewComponent
            },
            {
                path: 'groups',
                component: GroupsComponent
            },
            {
                path: 'credentials',
                component: CredentialsComponent
            },
            {
                path: 'share-account',
                component: CreateAccountFormComponent
            },
            {
                path: 'create-group',
                component: CreateGroupFormComponent
            }
        ]
    },
    {
        path: '**',
        component: NotFoundComponent
    }
];