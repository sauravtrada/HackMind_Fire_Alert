$uniqueEmail = "test_reg_$(Get-Random)@example.com"
$regBody = @{
    name = "Reg Test User"
    email = $uniqueEmail
    password = "password123"
} | ConvertTo-Json

Write-Output "Testing Registration for $uniqueEmail..."
$regResp = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/register" -Method Post -ContentType "application/json" -Body $regBody

Write-Output "Registration Response: $($regResp | ConvertTo-Json)"

if ($regResp.token) {
    Write-Error "FAILURE: Registration response still contains a token!"
} else {
    Write-Output "SUCCESS: No token found in registration response."
}

Write-Output "`nTesting Login for $uniqueEmail..."
$loginBody = @{
    email = $uniqueEmail
    password = "password123"
} | ConvertTo-Json

$loginResp = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/login" -Method Post -ContentType "application/json" -Body $loginBody

if ($loginResp.token) {
    Write-Output "SUCCESS: Login response contains a token."
} else {
    Write-Error "FAILURE: Login response is missing the token!"
}
