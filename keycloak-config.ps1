# Configurações
$KEYCLOAK_URL = "http://localhost:8080"
$ADMIN_USERNAME = "admin"
$ADMIN_PASSWORD = "admin"
$REALM_NAME = "leonardosr"
$CLIENT_ID = "auth-service"

# Obter token de acesso do admin
Write-Host "Obtendo token de acesso do admin..."
$tokenResponse = Invoke-RestMethod -Method Post -Uri "$KEYCLOAK_URL/realms/master/protocol/openid-connect/token" `
    -ContentType "application/x-www-form-urlencoded" `
    -Body @{
        username = $ADMIN_USERNAME
        password = $ADMIN_PASSWORD
        grant_type = "password"
        client_id = "admin-cli"
    }

$ADMIN_TOKEN = $tokenResponse.access_token

# Criar realm
Write-Host "Criando realm $REALM_NAME..."
$realmBody = @{
    realm = $REALM_NAME
    enabled = $true
    displayName = "Leonardo SR"
    registrationAllowed = $false
    sslRequired = "external"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "$KEYCLOAK_URL/admin/realms" `
    -Headers @{ Authorization = "Bearer $ADMIN_TOKEN" } `
    -ContentType "application/json" `
    -Body $realmBody

# Criar cliente
Write-Host "Criando cliente $CLIENT_ID..."
$clientBody = @{
    clientId = $CLIENT_ID
    enabled = $true
    publicClient = $true
    standardFlowEnabled = $true
    directAccessGrantsEnabled = $true
    serviceAccountsEnabled = $true
    authorizationServicesEnabled = $true
    redirectUris = @("http://localhost:8083/*")
    webOrigins = @("http://localhost:8083")
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "$KEYCLOAK_URL/admin/realms/$REALM_NAME/clients" `
    -Headers @{ Authorization = "Bearer $ADMIN_TOKEN" } `
    -ContentType "application/json" `
    -Body $clientBody

# Criar roles
Write-Host "Criando roles..."
$userRoleBody = @{
    name = "USER"
    description = "Usuário comum"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles" `
    -Headers @{ Authorization = "Bearer $ADMIN_TOKEN" } `
    -ContentType "application/json" `
    -Body $userRoleBody

$adminRoleBody = @{
    name = "ADMIN"
    description = "Administrador do sistema"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles" `
    -Headers @{ Authorization = "Bearer $ADMIN_TOKEN" } `
    -ContentType "application/json" `
    -Body $adminRoleBody

Write-Host "Configuração do Keycloak concluída!" 