try {
    $uniqueEmail = "temp_tester_$(Get-Random)@example.com"
    $regBody = @{
        name = "Temp Tester"
        email = $uniqueEmail
        password = "password123"
    } | ConvertTo-Json

    Write-Output "Registering user..."
    $regResp = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/register" -Method Post -ContentType "application/json" -Body $regBody
    
    $loginBody = @{
        email = $uniqueEmail
        password = "password123"
    } | ConvertTo-Json

    Write-Output "Logging in..."
    $loginResp = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/login" -Method Post -ContentType "application/json" -Body $loginBody
    $token = $loginResp.token

    $headers = @{
        Authorization = "Bearer $token"
    }

    $targetEmail = "saurav10trada@gmail.com"
    Write-Output "Sending alert to $targetEmail..."
    $alertResp = Invoke-RestMethod -Uri "http://localhost:8010/api/alerts/fire?email=$targetEmail" -Method Post -Headers $headers
    Write-Output "Response: $alertResp"

} catch {
    Write-Error "Failed: $_"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $body = $reader.ReadToEnd()
        Write-Output "Error Body: $body"
    }
}
