$loginBody = @{
    email = "test@example.com"
    password = "password123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/login" -Method Post -ContentType "application/json" -Body $loginBody
$token = $response.token
Write-Output "Token: $token"

$headers = @{
    Authorization = "Bearer $token"
}

$me = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/me" -Method Get -Headers $headers
Write-Output "Profile: $($me | ConvertTo-Json)"

$updateBody = @{
    name = "Updated Test User"
} | ConvertTo-Json

$updated = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/me" -Method Put -Headers $headers -ContentType "application/json" -Body $updateBody
Write-Output "Updated Profile: $($updated | ConvertTo-Json)"
