# OpenAPI YAML Files

Place your OpenAPI specification YAML files in this directory.

Expected files:
- `auth-api.yaml` - Authentication service API
- `catalog-api.yaml` - Catalog service API  
- `cart-api.yaml` - Cart service API

## Usage

1. Add your YAML files to this directory
2. Run the generation script: `npm run generate-client`
3. Import the generated clients from `../client/<service-name>`

## Example

```typescript
import { DefaultApi as CatalogApi } from '../client/catalog';
import { Configuration } from '../client/catalog';

const config = new Configuration({
  basePath: 'http://localhost:8080'
});

const catalogApi = new CatalogApi(config);
```