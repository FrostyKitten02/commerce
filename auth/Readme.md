# Auth WS

## Handles authentication and authorization for the application.
- All authorities are stored in JWT so any application can then use it to verify user and permission
- Authorities are stored in JWT as a list of strings in claim "auth"

## Example check for role or permission on method in spring boot
- you have to enable method security with this annotation @EnableMethodSecurity
- then on method you can use @PreAuthorize("hasRole('ADMIN')"), this will check for ROLE_ADMIN, or @PreAuthorize("hasAuthority('ADMIN')") and this only check for ADMIN authority