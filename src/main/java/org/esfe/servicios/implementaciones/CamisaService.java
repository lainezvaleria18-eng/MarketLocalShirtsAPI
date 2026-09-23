package org.esfe.servicios.implementaciones;

import org.esfe.dtos.camisa.CamisaGuardar;
import org.esfe.dtos.camisa.CamisaModificar;
import org.esfe.dtos.camisa.CamisaSalida;
import org.esfe.modelos.Camisa;
import org.esfe.modelos.Categoria;
import org.esfe.modelos.Color;
import org.esfe.modelos.ImagenProducto;
import org.esfe.modelos.Marca;
import org.esfe.modelos.ProductoColor;
import org.esfe.modelos.ProductoTalla;
import org.esfe.modelos.Talla;
import org.esfe.repositorios.ICamisaRepository;
import org.esfe.repositorios.ICategoriaRepository;
import org.esfe.repositorios.IColorRepository;
import org.esfe.repositorios.IImagenProductoRepository;
import org.esfe.repositorios.IMarcaRepository;
import org.esfe.repositorios.IProductoColorRepository;
import org.esfe.repositorios.IProductoTallaRepository;
import org.esfe.repositorios.ITallaRepository;
import org.esfe.servicios.interfaces.ICamisaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CamisaService implements ICamisaService {

    @Autowired
    private ICamisaRepository camisaRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IMarcaRepository marcaRepository;

    @Autowired
    private ITallaRepository tallaRepository;

    @Autowired
    private IColorRepository colorRepository;

    @Autowired
    private IProductoTallaRepository productoTallaRepository;

    @Autowired
    private IProductoColorRepository productoColorRepository;

    @Autowired
    private IImagenProductoRepository imagenProductoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CamisaSalida> obtenerTodos() {
        return camisaRepository.findAll().stream()
                .map(this::aSalida)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CamisaSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Camisa> page = camisaRepository.findAll(pageable);
        List<CamisaSalida> camisasDto = page.getContent().stream()
                .map(this::aSalida)
                .collect(Collectors.toList());
        return new PageImpl<>(camisasDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public CamisaSalida obtenerPorId(Integer id) {
        return camisaRepository.findById(id).map(this::aSalida).orElse(null);
    }

    @Override
    public List<CamisaSalida> buscarPorNombre(String nombre) {
        return camisaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::aSalida)
                .collect(Collectors.toList());
    }

    @Override
    public List<CamisaSalida> obtenerPorCategoria(Integer categoriaId) {
        return camisaRepository.findByCategoriaId(categoriaId).stream()
                .map(this::aSalida)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CamisaSalida crear(CamisaGuardar camisaGuardar) {
        Camisa camisa = new Camisa();
        camisa.setNombre(camisaGuardar.getNombre());
        camisa.setDescripcion(camisaGuardar.getDescripcion());
        camisa.setPrecio(camisaGuardar.getPrecio());
        camisa.setEstado(true);
        camisa.setCategoria(obtenerCategoria(camisaGuardar.getCategoriaId()));
        camisa.setMarca(obtenerMarca(camisaGuardar.getMarcaId()));
        camisa = camisaRepository.save(camisa);
        guardarRelaciones(camisa, camisaGuardar.getTalla(), camisaGuardar.getStock(), camisaGuardar.getColor(), camisaGuardar.getImagenUrl());
        return aSalida(camisa);
    }

    @Override
    @Transactional
    public CamisaSalida editar(CamisaModificar camisaModificar) {
        Camisa camisa = camisaRepository.findById(camisaModificar.getId()).orElse(null);
        if (camisa == null) {
            return null;
        }
        camisa.setNombre(camisaModificar.getNombre());
        camisa.setDescripcion(camisaModificar.getDescripcion());
        camisa.setPrecio(camisaModificar.getPrecio());
        camisa.setCategoria(obtenerCategoria(camisaModificar.getCategoriaId()));
        camisa.setMarca(obtenerMarca(camisaModificar.getMarcaId()));
        camisa = camisaRepository.save(camisa);
        guardarRelaciones(camisa, camisaModificar.getTalla(), camisaModificar.getStock(), camisaModificar.getColor(), camisaModificar.getImagenUrl());
        return aSalida(camisa);
    }

    @Override
    public void eliminarPorId(Integer id) {
        camisaRepository.deleteById(id);
    }

    private void guardarRelaciones(Camisa camisa, String nombreTalla, Integer stock, String nombreColor, String imagenUrl) {
        if (nombreTalla != null && !nombreTalla.isBlank()) {
            Talla talla = tallaRepository.findByNombreIgnoreCase(nombreTalla).orElseGet(() -> {
                Talla nueva = new Talla();
                nueva.setNombre(nombreTalla);
                return tallaRepository.save(nueva);
            });
            ProductoTalla productoTalla = productoTallaRepository
                    .findByProductoIdAndTallaId(camisa.getId(), talla.getId())
                    .orElseGet(ProductoTalla::new);
            productoTalla.setProducto(camisa);
            productoTalla.setTalla(talla);
            productoTalla.setStock(stock != null ? stock : 0);
            productoTallaRepository.save(productoTalla);
        }

        if (nombreColor != null && !nombreColor.isBlank()) {
            Color color = colorRepository.findByNombreIgnoreCase(nombreColor).orElseGet(() -> {
                Color nuevo = new Color();
                nuevo.setNombre(nombreColor);
                return colorRepository.save(nuevo);
            });
            boolean existe = productoColorRepository.findByProductoId(camisa.getId()).stream()
                    .anyMatch(pc -> pc.getColor().getId().equals(color.getId()));
            if (!existe) {
                ProductoColor productoColor = new ProductoColor();
                productoColor.setProducto(camisa);
                productoColor.setColor(color);
                productoColorRepository.save(productoColor);
            }
        }

        if (imagenUrl != null && !imagenUrl.isBlank()) {
            boolean existe = imagenProductoRepository.findByProductoId(camisa.getId()).stream()
                    .anyMatch(img -> imagenUrl.equals(img.getUrlImagen()));
            if (!existe) {
                ImagenProducto imagen = new ImagenProducto();
                imagen.setProducto(camisa);
                imagen.setUrlImagen(imagenUrl);
                imagen.setOrden(1);
                imagen.setPrincipal(true);
                imagenProductoRepository.save(imagen);
            }
        }
    }

    private CamisaSalida aSalida(Camisa camisa) {
        CamisaSalida salida = modelMapper.map(camisa, CamisaSalida.class);
        List<ProductoTalla> tallas = productoTallaRepository.findByProductoId(camisa.getId());
        int stock = 0;
        if (!tallas.isEmpty()) {
            ProductoTalla primera = tallas.get(0);
            stock = primera.getStock() == null ? 0 : primera.getStock();
            if (primera.getTalla() != null) {
                salida.setTalla(primera.getTalla().getNombre());
            }
        }
        salida.setStock(stock);
        salida.setAgotado(stock <= 0);
        List<ProductoColor> colores = productoColorRepository.findByProductoId(camisa.getId());
        if (!colores.isEmpty() && colores.get(0).getColor() != null) {
            salida.setColor(colores.get(0).getColor().getNombre());
        }
        List<ImagenProducto> imagenes = imagenProductoRepository.findByProductoId(camisa.getId());
        if (!imagenes.isEmpty()) {
            salida.setImagenUrl(imagenes.get(0).getUrlImagen());
        }
        return salida;
    }

    private Categoria obtenerCategoria(Integer categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaRepository.findById(categoriaId).orElse(null);
    }

    private Marca obtenerMarca(Integer marcaId) {
        if (marcaId == null) {
            return null;
        }
        return marcaRepository.findById(marcaId).orElse(null);
    }
}
