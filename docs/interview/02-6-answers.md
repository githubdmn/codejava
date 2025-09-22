I'll provide detailed answers to these Angular questions, focusing on integration with Spring Boot backends:

## **Q1: Angular Communication with Spring Boot Backend**

**HTTP Client Setup and Configuration:**

```typescript
// app.module.ts
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';

@NgModule({
  imports: [
    HttpClientModule,
    // other imports
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    }
  ]
})
export class AppModule { }
```


**Service Layer for API Communication:**

```typescript
// customer.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private readonly apiUrl = 'http://localhost:8080/api/v1/customers';

  constructor(private http: HttpClient) {}

  // GET requests
  getCustomers(page: number = 0, size: number = 20, filters?: CustomerFilter): Observable<PagedResponse<Customer>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (filters) {
      if (filters.firstName) params = params.set('firstName', filters.firstName);
      if (filters.email) params = params.set('email', filters.email);
      if (filters.active !== undefined) params = params.set('active', filters.active.toString());
    }

    return this.http.get<PagedResponse<Customer>>(this.apiUrl, { params })
      .pipe(
        retry(2), // Retry failed requests twice
        catchError(this.handleError)
      );
  }

  getCustomerById(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  // POST requests
  createCustomer(customer: CustomerCreateRequest): Observable<Customer> {
    return this.http.post<Customer>(this.apiUrl, customer)
      .pipe(
        catchError(this.handleError)
      );
  }

  // PUT requests
  updateCustomer(id: number, customer: CustomerUpdateRequest): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrl}/${id}`, customer)
      .pipe(catchError(this.handleError));
  }

  // DELETE requests
  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  // File upload
  uploadCustomerDocument(customerId: number, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    
    return this.http.post(`${this.apiUrl}/${customerId}/documents`, formData, {
      reportProgress: true,
      observe: 'events'
    }).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Client Error: ${error.error.message}`;
    } else {
      // Server-side error
      if (error.status === 400 && error.error.validationErrors) {
        // Handle validation errors from Spring Boot
        errorMessage = error.error.validationErrors
          .map((err: ValidationError) => `${err.field}: ${err.message}`)
          .join(', ');
      } else if (error.error && error.error.message) {
        errorMessage = error.error.message;
      } else {
        errorMessage = `Server Error Code: ${error.status}, Message: ${error.message}`;
      }
    }
    
    console.error('HTTP Error:', error);
    return throwError(() => new Error(errorMessage));
  }
}
```


**HTTP Interceptors for Cross-Cutting Concerns:**

```typescript
// auth.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();
    
    if (token) {
      const authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      return next.handle(authReq);
    }
    
    return next.handle(req);
  }
}

// error.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { NotificationService } from './notification.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private notificationService: NotificationService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Unauthorized - redirect to login
          this.router.navigate(['/login']);
        } else if (error.status === 403) {
          // Forbidden - show error message
          this.notificationService.showError('You do not have permission to perform this action');
        } else if (error.status >= 500) {
          // Server error
          this.notificationService.showError('Server error occurred. Please try again later.');
        }
        
        return throwError(() => error);
      })
    );
  }
}
```


**Environment Configuration:**

```typescript
// environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1',
  wsUrl: 'ws://localhost:8080/ws'
};

// environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'https://api.yourcompany.com/v1',
  wsUrl: 'wss://api.yourcompany.com/ws'
};
```


## **Q2: Structuring Services and Components for Reusability**

**Feature-Based Module Structure:**

```
src/app/
├── core/                    # Singleton services, guards, interceptors
│   ├── services/
│   │   ├── auth.service.ts
│   │   ├── notification.service.ts
│   │   └── loading.service.ts
│   ├── guards/
│   │   ├── auth.guard.ts
│   │   └── role.guard.ts
│   └── interceptors/
├── shared/                  # Reusable components, pipes, directives
│   ├── components/
│   │   ├── data-table/
│   │   ├── confirmation-dialog/
│   │   ├── loading-spinner/
│   │   └── form-field-error/
│   ├── pipes/
│   ├── directives/
│   └── shared.module.ts
├── features/                # Feature modules
│   ├── customers/
│   │   ├── components/
│   │   ├── services/
│   │   ├── models/
│   │   ├── customer-routing.module.ts
│   │   └── customer.module.ts
│   └── orders/
└── layout/                  # Layout components
    ├── header/
    ├── sidebar/
    └── footer/
```


**Reusable Component Design:**

```typescript
// shared/components/data-table/data-table.component.ts
import { Component, Input, Output, EventEmitter, TemplateRef, ContentChild } from '@angular/core';

export interface TableColumn {
  key: string;
  label: string;
  sortable?: boolean;
  type?: 'text' | 'number' | 'date' | 'currency' | 'custom';
  width?: string;
}

export interface TableAction {
  label: string;
  icon?: string;
  color?: 'primary' | 'warn' | 'accent';
  action: (item: any) => void;
  visible?: (item: any) => boolean;
}

@Component({
  selector: 'app-data-table',
  template: `
    <div class="data-table-container">
      <div class="table-header" *ngIf="showSearch || showColumnToggle">
        <mat-form-field *ngIf="showSearch" appearance="outline">
          <mat-label>Search</mat-label>
          <input matInput (keyup)="onSearch($event)" placeholder="Search...">
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
        
        <button mat-button *ngIf="showColumnToggle" [matMenuTriggerFor]="columnMenu">
          <mat-icon>view_column</mat-icon>
          Columns
        </button>
      </div>

      <table mat-table [dataSource]="dataSource" class="data-table" matSort
             (matSortChange)="onSort($event)">
        
        <ng-container [matColumnDef]="col.key" *ngFor="let col of visibleColumns">
          <th mat-header-cell *matHeaderCellDef 
              [mat-sort-header]="col.sortable ? col.key : null">
            {{ col.label }}
          </th>
          <td mat-cell *matCellDef="let item">
            <ng-container [ngSwitch]="col.type">
              <span *ngSwitchCase="'currency'">{{ getValue(item, col.key) | currency }}</span>
              <span *ngSwitchCase="'date'">{{ getValue(item, col.key) | date }}</span>
              <span *ngSwitchCase="'number'">{{ getValue(item, col.key) | number }}</span>
              <ng-container *ngSwitchCase="'custom'">
                <ng-container *ngTemplateOutlet="customCellTemplate; context: { $implicit: item, column: col }">
                </ng-container>
              </ng-container>
              <span *ngSwitchDefault>{{ getValue(item, col.key) }}</span>
            </ng-container>
          </td>
        </ng-container>

        <ng-container matColumnDef="actions" *ngIf="actions.length > 0">
          <th mat-header-cell *matHeaderCellDef>Actions</th>
          <td mat-cell *matCellDef="let item">
            <button mat-icon-button 
                    *ngFor="let action of getVisibleActions(item)"
                    [color]="action.color"
                    (click)="action.action(item)"
                    [matTooltip]="action.label">
              <mat-icon>{{ action.icon }}</mat-icon>
            </button>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"
            [class.selected]="selection.isSelected(row)"
            (click)="toggleSelection(row)"></tr>
      </table>

      <mat-paginator [length]="totalElements"
                     [pageSize]="pageSize"
                     [pageSizeOptions]="[10, 25, 50, 100]"
                     (page)="onPageChange($event)">
      </mat-paginator>
    </div>

    <!-- Content projection for custom cell templates -->
    <ng-content select="[slot=customCell]"></ng-content>
  `
})
export class DataTableComponent<T> {
  @Input() columns: TableColumn[] = [];
  @Input() data: T[] = [];
  @Input() actions: TableAction[] = [];
  @Input() loading = false;
  @Input() totalElements = 0;
  @Input() pageSize = 20;
  @Input() showSearch = true;
  @Input() showColumnToggle = true;
  @Input() selectable = false;

  @Output() pageChange = new EventEmitter<any>();
  @Output() sortChange = new EventEmitter<any>();
  @Output() searchChange = new EventEmitter<string>();
  @Output() selectionChange = new EventEmitter<T[]>();

  @ContentChild('customCell', { static: false }) customCellTemplate: TemplateRef<any>;

  dataSource = new MatTableDataSource<T>();
  selection = new SelectionModel<T>(true, []);
  visibleColumns: TableColumn[] = [];
  displayedColumns: string[] = [];

  ngOnInit() {
    this.visibleColumns = [...this.columns];
    this.updateDisplayedColumns();
    this.dataSource.data = this.data;
  }

  private updateDisplayedColumns() {
    this.displayedColumns = this.visibleColumns.map(col => col.key);
    if (this.actions.length > 0) {
      this.displayedColumns.push('actions');
    }
  }

  getValue(item: T, key: string): any {
    return key.split('.').reduce((obj, prop) => obj && obj[prop], item);
  }

  getVisibleActions(item: T): TableAction[] {
    return this.actions.filter(action => 
      !action.visible || action.visible(item)
    );
  }

  onPageChange(event: any) {
    this.pageChange.emit(event);
  }

  onSort(event: any) {
    this.sortChange.emit(event);
  }

  onSearch(event: any) {
    const searchTerm = event.target.value;
    this.searchChange.emit(searchTerm);
  }

  toggleSelection(item: T) {
    if (this.selectable) {
      this.selection.toggle(item);
      this.selectionChange.emit(this.selection.selected);
    }
  }
}
```


**Smart/Dumb Component Pattern:**

```typescript
// Smart Component (Container)
@Component({
  selector: 'app-customer-list',
  template: `
    <app-data-table
      [columns]="tableColumns"
      [data]="customers"
      [actions]="tableActions"
      [loading]="loading"
      [totalElements]="totalElements"
      (pageChange)="onPageChange($event)"
      (sortChange)="onSortChange($event)"
      (searchChange)="onSearchChange($event)">
    </app-data-table>
  `
})
export class CustomerListComponent implements OnInit {
  customers: Customer[] = [];
  loading = false;
  totalElements = 0;
  
  tableColumns: TableColumn[] = [
    { key: 'firstName', label: 'First Name', sortable: true },
    { key: 'lastName', label: 'Last Name', sortable: true },
    { key: 'email', label: 'Email', sortable: true },
    { key: 'registrationDate', label: 'Registration', type: 'date', sortable: true }
  ];

  tableActions: TableAction[] = [
    { 
      label: 'Edit', 
      icon: 'edit', 
      color: 'primary',
      action: (customer) => this.editCustomer(customer) 
    },
    { 
      label: 'Delete', 
      icon: 'delete', 
      color: 'warn',
      action: (customer) => this.deleteCustomer(customer),
      visible: (customer) => !customer.hasActiveOrders 
    }
  ];

  constructor(private customerService: CustomerService) {}

  ngOnInit() {
    this.loadCustomers();
  }

  private loadCustomers(filters?: any) {
    this.loading = true;
    this.customerService.getCustomers(0, 20, filters).subscribe({
      next: (response) => {
        this.customers = response.content;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading customers:', error);
        this.loading = false;
      }
    });
  }

  onPageChange(event: any) {
    // Handle pagination
  }

  onSortChange(event: any) {
    // Handle sorting
  }

  onSearchChange(searchTerm: string) {
    // Handle search
  }

  editCustomer(customer: Customer) {
    // Navigation or modal logic
  }

  deleteCustomer(customer: Customer) {
    // Confirmation and deletion logic
  }
}
```


**Service Pattern for Business Logic:**

```typescript
// Abstract base service for common CRUD operations
export abstract class BaseApiService<T, CreateRequest, UpdateRequest> {
  protected abstract apiUrl: string;

  constructor(protected http: HttpClient) {}

  getAll(params?: any): Observable<PagedResponse<T>> {
    return this.http.get<PagedResponse<T>>(this.apiUrl, { params })
      .pipe(catchError(this.handleError));
  }

  getById(id: number): Observable<T> {
    return this.http.get<T>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  create(item: CreateRequest): Observable<T> {
    return this.http.post<T>(this.apiUrl, item)
      .pipe(catchError(this.handleError));
  }

  update(id: number, item: UpdateRequest): Observable<T> {
    return this.http.put<T>(`${this.apiUrl}/${id}`, item)
      .pipe(catchError(this.handleError));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  protected handleError(error: HttpErrorResponse): Observable<never> {
    // Common error handling logic
    return throwError(() => error);
  }
}

// Specific service extending base service
@Injectable({
  providedIn: 'root'
})
export class CustomerService extends BaseApiService<Customer, CustomerCreateRequest, CustomerUpdateRequest> {
  protected apiUrl = `${environment.apiUrl}/customers`;

  constructor(http: HttpClient) {
    super(http);
  }

  // Additional customer-specific methods
  getCustomerOrders(customerId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/${customerId}/orders`)
      .pipe(catchError(this.handleError));
  }
}
```


## **Q3: RxJS Observables vs Promises**

**Why Observables Over Promises:**

**1. Multiple Values Over Time**
```typescript
// Promise - single value
const fetchData = (): Promise<Customer> => {
  return fetch('/api/customers/1').then(response => response.json());
};

// Observable - stream of values
const customerUpdates$ = new WebSocketSubject('ws://localhost:8080/customers/1');
customerUpdates$.subscribe(customer => {
  // Receives real-time updates
  console.log('Customer updated:', customer);
});
```


**2. Cancellation**
```typescript
// Observable can be cancelled
@Component({...})
export class CustomerComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  ngOnInit() {
    // Subscription automatically cancelled on component destroy
    this.customerService.getCustomer(1)
      .pipe(takeUntil(this.destroy$))
      .subscribe(customer => {
        this.customer = customer;
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// Promise cannot be cancelled
const promise = fetch('/api/customers/1');
// No way to cancel this request
```


**3. Powerful Operators**
```typescript
@Injectable()
export class CustomerSearchService {
  private searchTerms = new Subject<string>();

  // Debounced search with error handling and retry
  customers$ = this.searchTerms.pipe(
    debounceTime(300),           // Wait 300ms after user stops typing
    distinctUntilChanged(),      // Only emit if value changed
    filter(term => term.length >= 2), // Only search if term is 2+ chars
    switchMap(term =>            // Cancel previous request, start new one
      this.customerService.searchCustomers(term).pipe(
        retry(2),                // Retry failed requests 2 times
        catchError(error => {
          console.error('Search error:', error);
          return of([]);         // Return empty array on error
        })
      )
    ),
    shareReplay(1)              // Share result with multiple subscribers
  );

  search(term: string): void {
    this.searchTerms.next(term);
  }
}

// Component usage
@Component({
  template: `
    <input (keyup)="search(searchBox.value)" #searchBox>
    <div *ngFor="let customer of customers$ | async">
      {{ customer.name }}
    </div>
  `
})
export class SearchComponent {
  customers$ = this.searchService.customers$;

  constructor(private searchService: CustomerSearchService) {}

  search(term: string): void {
    this.searchService.search(term);
  }
}
```


**4. Composability and Reactive Patterns**
```typescript
@Component({...})
export class DashboardComponent implements OnInit {
  // Combine multiple data sources
  dashboardData$ = combineLatest([
    this.customerService.getCustomers(),
    this.orderService.getRecentOrders(),
    this.analyticsService.getSalesData()
  ]).pipe(
    map(([customers, orders, sales]) => ({
      totalCustomers: customers.totalElements,
      recentOrders: orders.content,
      salesAmount: sales.totalAmount
    })),
    catchError(error => {
      console.error('Dashboard error:', error);
      return of(null);
    })
  );

  // Real-time updates
  notifications$ = this.websocketService.connect().pipe(
    filter(message => message.type === 'NOTIFICATION'),
    map(message => message.data)
  );

  ngOnInit() {
    // Reactive data binding
    this.dashboardData$.subscribe(data => {
      if (data) {
        this.updateCharts(data);
      }
    });

    // Handle notifications
    this.notifications$.subscribe(notification => {
      this.showNotification(notification);
    });
  }
}
```


**5. Error Handling and Retry Logic**
```typescript
@Injectable()
export class DataService {
  getData(): Observable<any> {
    return this.http.get('/api/data').pipe(
      retryWhen(errors =>
        errors.pipe(
          delay(1000),              // Wait 1 second before retry
          take(3),                  // Maximum 3 retries
          tap(error => console.log('Retrying request:', error))
        )
      ),
      catchError(error => {
        if (error.status === 404) {
          return of(null);          // Return null for 404
        }
        return throwError(() => error); // Re-throw other errors
      })
    );
  }
}
```


## **Q4: Authentication Implementation with Spring Security**

**Authentication Service:**

```typescript
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'current_user';

  constructor(
    private http: HttpClient,
    private router: Router,
    private jwtHelper: JwtHelperService
  ) {
    // Initialize user from localStorage on service creation
    this.loadUserFromStorage();
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/login', credentials)
      .pipe(
        tap(response => {
          // Store token and user info
          localStorage.setItem(this.TOKEN_KEY, response.token);
          localStorage.setItem(this.USER_KEY, JSON.stringify(response.user));
          this.currentUserSubject.next(response.user);
        }),
        catchError(this.handleAuthError)
      );
  }

  logout(): void {
    // Clear local storage
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    
    // Update current user
    this.currentUserSubject.next(null);
    
    // Optional: Call backend logout endpoint
    this.http.post('/api/auth/logout', {}).subscribe();
    
    // Redirect to login
    this.router.navigate(['/login']);
  }

  refreshToken(): Observable<AuthResponse> {
    const refreshToken = localStorage.getItem('refresh_token');
    return this.http.post<AuthResponse>('/api/auth/refresh', { refreshToken })
      .pipe(
        tap(response => {
          localStorage.setItem(this.TOKEN_KEY, response.token);
          this.currentUserSubject.next(response.user);
        }),
        catchError(error => {
          this.logout(); // Force logout on refresh failure
          return throwError(() => error);
        })
      );
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return token != null && !this.jwtHelper.isTokenExpired(token);
  }

  hasRole(role: string): boolean {
    const user = this.currentUserSubject.value;
    return user?.roles?.includes(role) || false;
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  private loadUserFromStorage(): void {
    const token = this.getToken();
    const userJson = localStorage.getItem(this.USER_KEY);
    
    if (token && !this.jwtHelper.isTokenExpired(token) && userJson) {
      try {
        const user = JSON.parse(userJson);
        this.currentUserSubject.next(user);
      } catch (error) {
        console.error('Error parsing stored user data:', error);
        this.logout();
      }
    }
  }

  private handleAuthError(error: HttpErrorResponse): Observable<never> {
    let message = 'Authentication failed';
    
    if (error.status === 401) {
      message = 'Invalid credentials';
    } else if (error.status === 403) {
      message = 'Access forbidden';
    } else if (error.error?.message) {
      message = error.error.message;
    }
    
    return throwError(() => new Error(message));
  }
}
```


**Auth Guard:**

```typescript
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.checkAuth(route, state.url);
  }

  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.checkAuth(route, state.url);
  }

  private checkAuth(route: ActivatedRouteSnapshot, url: string): Observable<boolean> {
    if (this.authService.isAuthenticated()) {
      // Check role-based access
      const requiredRoles = route.data?.['roles'] as string[];
      if (requiredRoles && requiredRoles.length > 0) {
        if (this.authService.hasAnyRole(requiredRoles)) {
          return of(true);
        } else {
          this.router.navigate(['/forbidden']);
          return of(false);
        }
      }
      return of(true);
    }

    // Store attempted URL for redirecting after login
    this.router.navigate(['/login'], { queryParams: { returnUrl: url } });
    return of(false);
  }
}

// Role-specific guard
@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const requiredRoles = route.data?.['roles'] as string[];
    
    if (requiredRoles && !this.authService.hasAnyRole(requiredRoles)) {
      this.router.navigate(['/forbidden']);
      return false;
    }
    
    return true;
  }
}
```


**JWT Interceptor with Token Refresh:**

```typescript
@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject = new BehaviorSubject<string | null>(null);

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Add token to request
    const token = this.authService.getToken();
    if (token) {
      request = this.addTokenToRequest(request, token);
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        // Handle 401 errors with token refresh
        if (error.status === 401 && token) {
          return this.handle401Error(request, next);
        }
        
        return throwError(() => error);
      })
    );
  }

  private addTokenToRequest(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authService.refreshToken().pipe(
        switchMap((response: AuthResponse) => {
          this.isRefreshing = false;
          this.refreshTokenSubject.next(response.token);
          return next.handle(this.addTokenToRequest(request, response.token));
        }),
        catchError(error => {
          this.isRefreshing = false;
          this.authService.logout();
          return throwError(() => error);
        })
      );
    }

    // Queue requests while refreshing
    return this.refreshTokenSubject.pipe(
      filter(token => token !== null),
      take(1),
      switchMap(token => next.handle(this.addTokenToRequest(request, token!)))
    );
  }
}
```


**Login Component:**

```typescript
@Component({
  selector: 'app-login',
  template: `
    <div class="login-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Login</mat-card-title>
        </mat-card-header>
        
        <mat-card-content>
          <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Email</mat-label>
              <input matInput formControlName="email" type="email" required>
              <mat-error *ngIf="loginForm.get('email')?.hasError('required')">
                Email is required
              </mat-error>
              <mat-error *ngIf="loginForm.get('email')?.hasError('email')">
                Please enter a valid email
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Password</mat-label>
              <input matInput formControlName="password" type="password" required>
              <mat-error *ngIf="loginForm.get('password')?.hasError('required')">
                Password is required
              </mat-error>
            </mat-form-field>

            <div class="form-actions">
              <button mat-raised-button color="primary" type="submit" 
                      [disabled]="loginForm.invalid || loading">
                <mat-spinner diameter="20" *ngIf="loading"></mat-spinner>
                <span *ngIf="!loading">Login</span>
              </button>
            </div>
          </form>
          
          <div class="error-message" *ngIf="errorMessage">
            {{ errorMessage }}
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  errorMessage = '';
  returnUrl = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Get return URL from route parameters or default to dashboard
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
    
    // Redirect if already logged in
    if (this.authService.isAuthenticated()) {
      this.router.navigate([this.returnUrl]);
    }
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      
      const credentials: LoginRequest = this.loginForm.value;
      
      this.authService.login(credentials).subscribe({
        next: (response) => {
          this.loading = false;
          this.router.navigate([this.returnUrl]);
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage = error.message || 'Login failed';
        }
      });
    }
  }
}
```


## **Q5: State Management Approaches**

**1. Service-Based State Management (Simple Applications):**

```typescript
// Simple state service
@Injectable({
  providedIn: 'root'
})
export class AppStateService {
  // User state
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  // Loading state
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  // Notification state
  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  // Shopping cart state (example)
  private cartItemsSubject = new BehaviorSubject<CartItem[]>([]);
  public cartItems$ = this.cartItemsSubject.asObservable();
  
  constructor() {
    // Load initial state from localStorage
    this.loadStateFromStorage();
  }

  // User state methods
  setCurrentUser(user: User | null): void {
    this.currentUserSubject.next(user);
    if (user) {
      localStorage.setItem('currentUser', JSON.stringify(user));
    } else {
      localStorage.removeItem('currentUser');
    }
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  // Loading state methods
  setLoading(loading: boolean): void {
    this.loadingSubject.next(loading);
  }

  // Cart methods
  addToCart(item: CartItem): void {
    const currentItems = this.cartItemsSubject.value;
    const existingItem = currentItems.find(i => i.productId === item.productId);
    
    if (existingItem) {
      existingItem.quantity += item.quantity;
      this.cartItemsSubject.next([...currentItems]);
    } else {
      this.cartItemsSubject.next([...currentItems, item]);
    }
    
    this.saveCartToStorage();
  }

  removeFromCart(productId: number): void {
    const currentItems = this.cartItemsSubject.value;
    const updatedItems = currentItems.filter(item => item.productId !== productId);
    this.cartItemsSubject.next(updatedItems);
    this.saveCartToStorage();
  }

  clearCart(): void {
    this.cartItemsSubject.next([]);
    localStorage.removeItem('cartItems');
  }

  getCartTotal(): Observable<number> {
    return this.cartItems$.pipe(
      map(items => items.reduce((total, item) => total + (item.price * item.quantity), 0))
    );
  }

  // Notification methods
  addNotification(notification: Notification): void {
    const current = this.notificationsSubject.value;
    this.notificationsSubject.next([...current, notification]);
  }

  removeNotification(id: string): void {
    const current = this.notificationsSubject.value;
    this.notificationsSubject.next(current.filter(n => n.id !== id));
  }

  private loadStateFromStorage(): void {
    // Load user
    const userJson = localStorage.getItem('currentUser');
    if (userJson) {
      try {
        const user = JSON.parse(userJson);
        this.currentUserSubject.next(user);
      } catch (error) {
        console.error('Error loading user from storage:', error);
      }
    }

    // Load cart
    const cartJson = localStorage.getItem('cartItems');
    if (cartJson) {
      try {
        const items = JSON.parse(cartJson);
        this.cartItemsSubject.next(items);
      } catch (error) {
        console.error('Error loading cart from storage:', error);
      }
    }
  }

  private saveCartToStorage(): void {
    const items = this.cartItemsSubject.value;
    localStorage.setItem('cartItems', JSON.stringify(items));
  }
}
```


**2. NgRx State Management (Complex Applications):**

```typescript
// State interface
export interface AppState {
  auth: AuthState;
  customers: CustomerState;
  orders: OrderState;
  ui: UiState;
}

// Customer state
export interface CustomerState {
  customers: Customer[];
  selectedCustomer: Customer | null;
  loading: boolean;
  error: string | null;
  filters: CustomerFilters;
  pagination: PaginationState;
}

// Customer actions
export const CustomerActions = createActionGroup({
  source: 'Customer',
  events: {
    'Load Customers': props<{ filters: CustomerFilters; page: number }>(),
    'Load Customers Success': props<{ response: PagedResponse<Customer> }>(),
    'Load Customers Failure': props<{ error: string }>(),
    
    'Select Customer': props<{ customer: Customer }>(),
    'Clear Selection': emptyProps(),
    
    'Create Customer': props<{ customer: CustomerCreateRequest }>(),
    'Create Customer Success': props<{ customer: Customer }>(),
    'Create Customer Failure': props<{ error: string }>(),
    
    'Update Customer': props<{ id: number; customer: CustomerUpdateRequest }>(),
    'Update Customer Success': props<{ customer: Customer }>(),
    'Update Customer Failure': props<{ error: string }>(),
    
    'Delete Customer': props<{ id: number }>(),
    'Delete Customer Success': props<{ id: number }>(),
    'Delete Customer Failure': props<{ error: string }>(),
    
    'Set Filters': props<{ filters: CustomerFilters }>(),
    'Clear Filters': emptyProps()
  }
});

// Customer reducer
const initialState: CustomerState = {
  customers: [],
  selectedCustomer: null,
  loading: false,
  error: null,
  filters: {},
  pagination: {
    page: 0,
    size: 20,
    totalElements: 0,
    totalPages: 0
  }
};

export const customerReducer = createReducer(
  initialState,
  
  on(CustomerActions.loadCustomers, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(CustomerActions.loadCustomersSuccess, (state, { response }) => ({
    ...state,
    customers: response.content,
    loading: false,
    error: null,
    pagination: {
      page: response.page,
      size: response.size,
      totalElements: response.totalElements,
      totalPages: response.totalPages
    }
  })),
  
  on(CustomerActions.loadCustomersFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  on(CustomerActions.selectCustomer, (state, { customer }) => ({
    ...state,
    selectedCustomer: customer
  })),
  
  on(CustomerActions.createCustomerSuccess, (state, { customer }) => ({
    ...state,
    customers: [...state.customers, customer]
  })),
  
  on(CustomerActions.updateCustomerSuccess, (state, { customer }) => ({
    ...state,
    customers: state.customers.map(c => c.id === customer.id ? customer : c),
    selectedCustomer: state.selectedCustomer?.id === customer.id ? customer : state.selectedCustomer
  })),
  
  on(CustomerActions.deleteCustomerSuccess, (state, { id }) => ({
    ...state,
    customers: state.customers.filter(c => c.id !== id),
    selectedCustomer: state.selectedCustomer?.id === id ? null : state.selectedCustomer
  })),
  
  on(CustomerActions.setFilters, (state, { filters }) => ({
    ...state,
    filters
  }))
);

// Customer effects
@Injectable()
export class CustomerEffects {
  loadCustomers$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.loadCustomers),
      switchMap(({ filters, page }) =>
        this.customerService.getCustomers(page, 20, filters).pipe(
          map(response => CustomerActions.loadCustomersSuccess({ response })),
          catchError(error => of(CustomerActions.loadCustomersFailure({ 
            error: error.message 
          })))
        )
      )
    )
  );

  createCustomer$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.createCustomer),
      switchMap(({ customer }) =>
        this.customerService.createCustomer(customer).pipe(
          map(newCustomer => CustomerActions.createCustomerSuccess({ customer: newCustomer })),
          catchError(error => of(CustomerActions.createCustomerFailure({ 
            error: error.message 
          })))
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private customerService: CustomerService
  ) {}
}

// Customer selectors
export const selectCustomerState = (state: AppState) => state.customers;

export const selectAllCustomers = createSelector(
  selectCustomerState,
  (state: CustomerState) => state.customers
);

export const selectSelectedCustomer = createSelector(
  selectCustomerState,
  (state: CustomerState) => state.selectedCustomer
);

export const selectCustomersLoading = createSelector(
  selectCustomerState,
  (state: CustomerState) => state.loading
);

export const selectCustomersPagination = createSelector(
  selectCustomerState,
  (state: CustomerState) => state.pagination
);

// Component usage
@Component({
  selector: 'app-customer-list',
  template: `
    <div class="customer-list">
      <div class="loading-indicator" *ngIf="loading$ | async">
        <mat-spinner></mat-spinner>
      </div>
      
      <app-data-table
        [data]="customers$ | async"
        [loading]="loading$ | async"
        [totalElements]="(pagination$ | async)?.totalElements || 0"
        (pageChange)="onPageChange($event)">
      </app-data-table>
    </div>
  `
})
export class CustomerListComponent implements OnInit {
  customers$ = this.store.select(selectAllCustomers);
  loading$ = this.store.select(selectCustomersLoading);
  pagination$ = this.store.select(selectCustomersPagination);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(CustomerActions.loadCustomers({ 
      filters: {}, 
      page: 0 
    }));
  }

  onPageChange(event: any): void {
    this.store.dispatch(CustomerActions.loadCustomers({ 
      filters: {}, 
      page: event.pageIndex 
    }));
  }
}
```


**3. LocalStorage with RxJS (Medium Complexity):**

```typescript
@Injectable({
  providedIn: 'root'
})
export class StorageStateService {
  private storageSubject = new Subject<{key: string, value: any}>();
  
  // Listen to storage changes across tabs
  private storageListener$ = fromEvent(window, 'storage').pipe(
    map((event: StorageEvent) => ({
      key: event.key,
      newValue: event.newValue,
      oldValue: event.oldValue
    }))
  );

  constructor() {
    // React to external storage changes (other tabs)
    this.storageListener$.subscribe(event => {
      if (event.key && event.newValue) {
        try {
          const value = JSON.parse(event.newValue);
          this.storageSubject.next({ key: event.key, value });
        } catch (error) {
          console.error('Error parsing storage value:', error);
        }
      }
    });
  }

  // Generic storage methods
  setItem<T>(key: string, value: T): void {
    try {
      localStorage.setItem(key, JSON.stringify(value));
      this.storageSubject.next({ key, value });
    } catch (error) {
      console.error('Error saving to localStorage:', error);
    }
  }

  getItem<T>(key: string): T | null {
    try {
      const value = localStorage.getItem(key);
      return value ? JSON.parse(value) : null;
    } catch (error) {
      console.error('Error reading from localStorage:', error);
      return null;
    }
  }

  removeItem(key: string): void {
    localStorage.removeItem(key);
    this.storageSubject.next({ key, value: null });
  }

  // Observable for specific key changes
  watchItem<T>(key: string): Observable<T | null> {
    return merge(
      // Initial value
      of(this.getItem<T>(key)),
      // Future changes
      this.storageSubject.pipe(
        filter(change => change.key === key),
        map(change => change.value)
      )
    ).pipe(distinctUntilChanged());
  }

  // Typed storage methods
  setUserPreferences(preferences: UserPreferences): void {
    this.setItem('userPreferences', preferences);
  }

  getUserPreferences(): Observable<UserPreferences | null> {
    return this.watchItem<UserPreferences>('userPreferences');
  }

  setShoppingCart(cart: CartItem[]): void {
    this.setItem('shoppingCart', cart);
  }

  getShoppingCart(): Observable<CartItem[]> {
    return this.watchItem<CartItem[]>('shoppingCart').pipe(
      map(cart => cart || [])
    );
  }
}
```


**Best Practices Summary:**

1. **Simple Apps**: Use service-based state management with BehaviorSubject
2. **Complex Apps**: Use NgRx for predictable state management
3. **LocalStorage**: Use for user preferences, shopping cart, offline data
4. **SessionStorage**: Use for temporary data within browser session
5. **Memory Only**: Use for UI state that doesn't need persistence
6. **Always handle errors** when reading/writing to storage
7. **Use RxJS operators** to create reactive, composable data flows
8. **Consider performance** - avoid unnecessary state updates and subscriptions

These patterns provide robust, scalable solutions for managing state in Angular applications while maintaining good integration with Spring Boot backends.