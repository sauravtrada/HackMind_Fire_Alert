$uniqueEmail = "test_$(Get-Random)@example.com"
$regBody = @{
    name = "New User"
    email = $uniqueEmail
    password = "password123"
} | ConvertTo-Json

Write-Output "Registering $uniqueEmail..."
$regResp = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/register" -Method Post -ContentType "application/json" -Body $regBody
$token = $regResp.token
Write-Output "Token: $token"

$headers = @{
    Authorization = "Bearer $token"
}

Write-Output "Fetching details..."
$me = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/me" -Method Get -Headers $headers
Write-Output "Profile: $($me | ConvertTo-Json)"

$updateBody = @{
    name = "Updated User Name"
} | ConvertTo-Json

Write-Output "Updating profile..."
$updated = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/me" -Method Put -Headers $headers -ContentType "application/json" -Body $updateBody
Write-Output "Updated Profile: $($updated | ConvertTo-Json)"
