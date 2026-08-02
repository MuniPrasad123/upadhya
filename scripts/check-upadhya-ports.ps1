$ErrorActionPreference = 'Stop'

$defaults = [ordered]@{
    UPADHYA_SERVER_PORT = 8091
    UPADHYA_POSTGRES_HOST_PORT = 55433
    UPADHYA_QDRANT_HTTP_PORT = 6433
    UPADHYA_QDRANT_GRPC_PORT = 6434
}

$envFile = Join-Path (Split-Path $PSScriptRoot -Parent) '.env'
$configured = @{}
if (Test-Path -LiteralPath $envFile) {
    Get-Content -LiteralPath $envFile | Where-Object { $_ -match '^\s*[^#][^=]*=' } | ForEach-Object {
        $key, $value = $_ -split '=', 2
        $configured[$key.Trim()] = $value.Trim()
    }
}

$occupied = $false
foreach ($item in $defaults.GetEnumerator()) {
    $port = if ($configured.ContainsKey($item.Key)) { [int]$configured[$item.Key] } else { $item.Value }
    $listener = Get-NetTCPConnection -State Listen -LocalPort $port -ErrorAction SilentlyContinue
    if ($listener) {
        Write-Error "Port $port ($($item.Key)) is already occupied. Choose a different value in Upadhya's .env file. No process was terminated." -ErrorAction Continue
        $occupied = $true
    } else {
        Write-Host "Available: $port ($($item.Key))"
    }
}

if ($occupied) { exit 1 }
Write-Host 'All configured Upadhya host ports are available.'
