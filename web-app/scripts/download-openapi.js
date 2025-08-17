#!/usr/bin/env node

import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const OPENAPI_DIR = path.join(__dirname, '..', 'openapi');

// Backend service configurations
const services = [
  {
    name: 'auth',
    url: 'http://localhost:8000/api/docs.yaml',
    outputFile: 'auth-api.yaml'
  },
  {
    name: 'catalog',
    url: 'http://localhost:8001/api/docs.yaml',
    outputFile: 'catalog-api.yaml'
  },
  {
    name: 'cart',
    url: 'http://localhost:8002/api/docs.yaml',
    outputFile: 'cart-api.yaml'
  }
];

async function downloadOpenApiSpec(service) {
  console.log(`🔄 Downloading ${service.name} OpenAPI spec...`);
  
  try {
    const response = await fetch(service.url, {
      headers: {
        'Accept': 'application/x-yaml, text/yaml, application/yaml, text/plain, */*',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        'Origin': 'http://localhost:5173',
        'Referer': 'http://localhost:5173/',
        'Cache-Control': 'no-cache'
      },
      method: 'GET',
      credentials: 'omit'
    });

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }

    const content = await response.text();
    const outputPath = path.join(OPENAPI_DIR, service.outputFile);
    fs.writeFileSync(outputPath, content, 'utf8');
    
    console.log(`✅ Successfully downloaded ${service.name} spec to ${service.outputFile}`);
    return true;
  } catch (error) {
    console.error(`❌ Failed to download ${service.name} spec:`, error.message);
    
    if (error.code === 'ECONNREFUSED') {
      console.error(`   Make sure the ${service.name} service is running on ${service.url}`);
    } else if (error.message.includes('403')) {
      console.error(`   403 Forbidden - Try manually downloading from browser:`);
      console.error(`   Browser: ${service.url}`);
      console.error(`   Save as: openapi/${service.outputFile}`);
    }
    
    return false;
  }
}

async function main() {
  console.log('🚀 Starting OpenAPI specification download from running backends...\n');

  // Create openapi directory if it doesn't exist
  if (!fs.existsSync(OPENAPI_DIR)) {
    fs.mkdirSync(OPENAPI_DIR, { recursive: true });
    console.log(`📁 Created openapi directory: ${OPENAPI_DIR}\n`);
  }

  // Download OpenAPI specs
  let successCount = 0;
  for (const service of services) {
    const success = await downloadOpenApiSpec(service);
    if (success) successCount++;
  }

  console.log(`\n🎉 Download completed! ${successCount}/${services.length} specs downloaded successfully.`);
  
  if (successCount > 0) {
    console.log('\n📖 Next steps:');
    console.log('  1. Review the downloaded YAML files in ./openapi/');
    console.log('  2. Run: npm run generate-client');
    console.log('  3. Use the generated TypeScript clients in your application');
  }

  if (successCount < services.length) {
    console.log('\n🔧 Troubleshooting:');
    console.log('  • Make sure all backend services are running');
    console.log('  • Check that SpringDoc is configured correctly');
    console.log('  • Verify the service URLs are accessible');
  }
}

main();