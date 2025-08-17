#!/usr/bin/env node

import { execSync } from 'child_process';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const YAML_DIR = path.join(__dirname, '..', 'openapi');
const OUTPUT_DIR = path.join(__dirname, '..', 'client');

// Configuration for each service
const services = [
  {
    name: 'auth',
    yamlFile: 'auth-api.yaml',
    packageName: 'auth-client'
  },
  {
    name: 'catalog', 
    yamlFile: 'catalog-api.yaml',
    packageName: 'catalog-client'
  },
  {
    name: 'cart',
    yamlFile: 'cart-api.yaml', 
    packageName: 'cart-client'
  },
  {
    name: 'checkout',
    yamlFile: 'checkout-api.yaml',
    packageName: 'checkout-client'
  }
];

function generateClient(service) {
  const yamlPath = path.join(YAML_DIR, service.yamlFile);
  const outputPath = path.join(OUTPUT_DIR, service.name);

  // Check if YAML file exists
  if (!fs.existsSync(yamlPath)) {
    console.warn(`⚠️  YAML file not found: ${yamlPath}`);
    console.warn(`   Skipping ${service.name} client generation`);
    return;
  }

  console.log(`🔄 Generating ${service.name} client...`);

  // Remove existing client directory
  if (fs.existsSync(outputPath)) {
    fs.rmSync(outputPath, { recursive: true, force: true });
  }

  try {
    // Generate TypeScript client using OpenAPI Generator
    const command = `npx openapi-generator-cli generate -i "${yamlPath}" -g typescript-axios -o "${outputPath}" --additional-properties=npmName=${service.packageName},supportsES6=true,withInterfaces=true,modelPropertyNaming=camelCase`;
    
    execSync(command, { 
      stdio: 'inherit',
      cwd: path.join(__dirname, '..')
    });

    console.log(`✅ Successfully generated ${service.name} client`);
  } catch (error) {
    console.error(`❌ Failed to generate ${service.name} client:`, error.message);
  }
}

function main() {
  console.log('🚀 Starting TypeScript client generation from YAML files...\n');

  // Create directories if they don't exist
  if (!fs.existsSync(YAML_DIR)) {
    fs.mkdirSync(YAML_DIR, { recursive: true });
    console.log(`📁 Created YAML directory: ${YAML_DIR}`);
  }

  if (!fs.existsSync(OUTPUT_DIR)) {
    fs.mkdirSync(OUTPUT_DIR, { recursive: true });
    console.log(`📁 Created output directory: ${OUTPUT_DIR}`);
  }

  // Generate clients for each service
  services.forEach(service => generateClient(service));

  console.log('\n🎉 Client generation completed!');
  console.log('\n📖 Usage:');
  console.log('  1. Place your OpenAPI YAML files in the ./openapi directory');
  console.log('  2. Run: npm run generate-client');
  console.log('  3. Import clients from ./client/<service-name>');
}

main();