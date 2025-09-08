import {NgModule} from '@angular/core';
import {AppComponent} from "./component/app/app.component";
import {provideRouter, RouterModule} from "@angular/router";
import {BrowserModule} from "@angular/platform-browser";
import {CommonModule} from "@angular/common";
import {provideHttpClient, withFetch} from "@angular/common/http";
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {routes} from "./app.routes";


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    RouterModule,
  ],
  providers: [
    provideHttpClient(withFetch()),
    provideAnimationsAsync(),
    provideRouter(routes)
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
